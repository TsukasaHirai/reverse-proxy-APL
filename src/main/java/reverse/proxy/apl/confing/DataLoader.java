package reverse.proxy.apl.confing;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.google.cloud.spanner.SpannerException;
import com.google.cloud.spring.data.spanner.core.admin.SpannerDatabaseAdminTemplate;
import com.google.cloud.spring.data.spanner.core.admin.SpannerSchemaUtils;
import com.google.cloud.spring.data.spanner.core.mapping.Table;

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
	
	private final SpannerDatabaseAdminTemplate spannerDatabaseAdminTemplate;

	private final SpannerSchemaUtils spannerSchemaUtils;

	private static final String DEFAULT_USERNAME = "test";

	private static final String DEFAULT_PASSWORD = "password";
	
	private static final Set<Class<?>> createTables = Set.of(Employee.class);

	/**
	 * 起動時に実行される処理。
	 */
	@Override
	public void run(String... args) throws Exception {
		createTable();
		saveDefaultUser();
	}

	
	/*
	 * アプリが使用するテーブルがない場合のみ、テーブルを作成する。
	 */
	private void createTable() throws SpannerException, InterruptedException, ExecutionException, IOException {
		createTables.stream()
				.filter(t -> t.isAnnotationPresent(Table.class)) 
				.forEach(t -> {
					String tableName = t.getAnnotation(Table.class).name();
					if (!this.spannerDatabaseAdminTemplate.tableExists(tableName)) {
						LOG.info("テーブルを作成します");
						this.spannerDatabaseAdminTemplate.executeDdlStrings(
								this.spannerSchemaUtils.getCreateTableDdlStringsForInterleavedHierarchy(t), true);
					}
				});
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
