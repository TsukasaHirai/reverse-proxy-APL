package reverse.proxy.apl.repository;

import java.util.Optional;

import com.google.cloud.spanner.Key;
import com.google.cloud.spring.data.spanner.repository.SpannerRepository;

import reverse.proxy.apl.entity.Employee;


public interface EmployeeRepository extends SpannerRepository<Employee, Key>{
	
	Optional<Employee> findByUsername(String username);
}
