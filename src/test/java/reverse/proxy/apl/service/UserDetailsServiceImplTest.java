package reverse.proxy.apl.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

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
	
	@MockBean
	private EmployeeRepository repository;
	
	@Autowired
	private UserDetailsServiceImpl target;

	@Test
	@DisplayName("ユーザー存在する場合、ユーザーを取得できるかどうかのテスト")
	void findByUserTest() {
		// データ準備
		String testUsername = "test";
		String testPassword = "password";
		
		Employee employee = new Employee();
		employee.setUsername(testUsername);
		employee.setPassword(testPassword);
		
		doAnswer(invocation -> Optional.of(employee)).when(repository).findByUsername(any());
		// Act
		UserDetails actual = target.loadUserByUsername(testUsername);
		
		// Assert
		assertEquals(employee.getUsername(), actual.getUsername());
	}
	
	@Test
	@DisplayName("ユーザーが存在しない場合、例外をスローするかのテスト")
	void notFoundByUserTest() {
		// Act & Assert
		assertThrows(UsernameNotFoundException.class, () -> target.loadUserByUsername("Not Found"));
	}

}
