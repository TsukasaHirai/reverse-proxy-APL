package reverse.proxy.apl.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import reverse.proxy.apl.confing.DataLoader;
import reverse.proxy.apl.entity.Employee;
import reverse.proxy.apl.repository.EmployeeRepository;

@SpringBootTest
class UserDetailsServiceImplTest {
	
	@MockBean
	private DataLoader dataLoader;
	
	@Autowired
	private EmployeeRepository repository;
	
	@Autowired
	private UserDetailsServiceImpl target;

	@DisplayName("ユーザー存在する場合、ユーザーを取得できるかどうかのテスト")
	void findByUserTest() {
		// Arrange
		String testUsername = "test";
		
		Employee employee = new Employee();
		employee.setUsername(testUsername);
		employee.setPassword("password");
		repository.save(employee);
		
		// Act
		UserDetails actual = target.loadUserByUsername(testUsername);
		
		// Assert
		assertEquals(employee.getUsername(), actual.getUsername());
		
		repository.delete(employee);
	}
	
	@Test
	@DisplayName("ユーザーが存在しない場合、例外をスローするかのテスト")
	void notFindByUserTest() {
		// Act & Assert
		assertThrows(UsernameNotFoundException.class, () -> target.loadUserByUsername("Not Found"));
	}

}
