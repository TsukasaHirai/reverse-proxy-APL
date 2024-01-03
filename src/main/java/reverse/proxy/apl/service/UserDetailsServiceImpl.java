package reverse.proxy.apl.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reverse.proxy.apl.model.Employee;
import reverse.proxy.apl.repository.EmployeeRepository;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	private final EmployeeRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Employee employee = repository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
		
		return User.withUsername(employee.getUsername()).password(employee.getPassword()).build();
	}
}
