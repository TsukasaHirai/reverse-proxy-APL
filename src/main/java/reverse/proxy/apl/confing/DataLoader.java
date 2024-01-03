package reverse.proxy.apl.confing;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import reverse.proxy.apl.model.Employee;
import reverse.proxy.apl.repository.EmployeeRepository;

/**
 * 起動時処理。
 */
@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {
	
	private final PasswordEncoder passwordEncoder; 
	
	private final EmployeeRepository repository;
	
	/**
	 * 起動時に実行される処理。
	 */
	@Override
	public void run(String... args) throws Exception {
		
		// H2にログインユーザを投入する。
		Employee employee = new Employee();
		employee.setUsername("user");
		employee.setPassword(passwordEncoder.encode("password"));
		
		repository.save(employee);
	}
}
