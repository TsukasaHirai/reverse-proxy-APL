package reverse.proxy.apl.confing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reverse.proxy.apl.entity.Employee;
import reverse.proxy.apl.repository.EmployeeRepository;

/**
 * 起動時処理。
 */
@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    private final EmployeeRepository repository;

    private static final String DEFAULT_USERNAME = "test";

    private static final String DEFAULT_PASSWORD = "password";

    /**
     * 起動時に実行される処理。
     */
    @Override
    public void run(String... args) throws Exception {
        saveDefaultUser();
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
