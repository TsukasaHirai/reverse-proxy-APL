package reverse.proxy.apl.confing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.google.cloud.spanner.DatabaseAdminClient;
import com.google.cloud.spanner.ResultSet;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerException;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.Statement;
import com.google.cloud.spring.autoconfigure.spanner.GcpSpannerProperties;
import com.google.cloud.spring.data.spanner.core.SpannerTemplate;

import lombok.RequiredArgsConstructor;
import reverse.proxy.apl.entity.Employee;
import reverse.proxy.apl.repository.EmployeeRepository;

/**
 * 起動時処理。
 */
@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

	private static final Logger LOG = Logger.getLogger(DataLoader.class.getName());

	private final PasswordEncoder passwordEncoder;

	private final EmployeeRepository repository;

	private final GcpSpannerProperties gcpSpannerProperties;

	private final SpannerTemplate spannerTemplate;

	private final SpannerOptions spannerOptions;

	private static final String CHECK_DDL_SQL = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'EMPLOYEE'";

	private static final String DEFAULT_USERNAME = "test";

	private static final String DEFAULT_PASSWORD = "password";

	/**
	 * 起動時に実行される処理。
	 */
	@Override
	public void run(String... args) throws Exception {
		createDDL();
		saveDefaultUser();
	}

	
	/*
	 * アプリが使用するDDLがない場合のみ、DDLを作成する。
	 */
	private void createDDL() throws SpannerException, InterruptedException, ExecutionException, IOException {
		
		long count;
		try (ResultSet resultSet = spannerTemplate.executeQuery(Statement.of(CHECK_DDL_SQL), null)) {
            resultSet.next();
			count = resultSet.getLong(0);
        }

		if (count > 0) {
			// テーブルを作る必要性がないため処理を終了
			LOG.info("アプリが使用するテーブルが作成されていました。");
			return;
		}
		try(InputStream inputStream = new ClassPathResource("schema.sql").getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			
			LOG.info("アプリが使用するテーブルがないため作成します");
			List<String> ddlStatements = new ArrayList<>();
			StringBuilder ddlStatement = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				ddlStatement.append(line).append(" ");
				if (line.trim().endsWith(";")) {
					// {@code ;}を含めるとエラーになるため削除
					ddlStatements.add(ddlStatement.toString().replace(";", "").trim());
					ddlStatement.setLength(0);
				}
			}
			Spanner spanner = spannerOptions.getService();
			DatabaseAdminClient adminClient = spanner.getDatabaseAdminClient();
			adminClient.updateDatabaseDdl(gcpSpannerProperties.getInstanceId(), gcpSpannerProperties.getDatabase(), ddlStatements, null).get();
		}
    }

	/**
	 * デフォルトユーザーを保存する。
	 */
	private void saveDefaultUser() {
		// ログインユーザを投入する。
		if (repository.findByUsername(DEFAULT_USERNAME).isPresent()) {
			// すでに登録済みの場合はスキップをする
			return;
		}
		Employee employee = new Employee();
		employee.setUsername(DEFAULT_USERNAME);
		employee.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));

		repository.save(employee);
	}
}
