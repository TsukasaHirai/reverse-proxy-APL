package reverse.proxy.apl.repository;

import java.util.Optional;

import com.google.cloud.spring.data.spanner.repository.SpannerRepository;

import reverse.proxy.apl.dto.Employee;


public interface EmployeeRepository extends SpannerRepository<Employee, String>{
	
	Optional<Employee> findByUsername(String username);
}
