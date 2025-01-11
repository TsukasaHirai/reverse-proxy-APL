package reverse.proxy.apl.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import reverse.proxy.apl.entity.Employee;
import reverse.proxy.apl.repository.EmployeeRepository;
import reverse.proxy.apl.util.TraceUtil;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final EmployeeRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Span span = TraceUtil.startSpan("loadUserByUsername");
		try (Scope scope = span.makeCurrent()) {
			Employee employee = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
			TraceUtil.putAttributeCurrentAndRootSpan(span, "username", username);
			return User.withUsername(employee.getUsername()).password(employee.getPassword()).build();
		} catch (UsernameNotFoundException e) {
			span.setStatus(StatusCode.ERROR, e.getMessage());
			throw e;
		} finally {
			span.end();
		}
	}
}
