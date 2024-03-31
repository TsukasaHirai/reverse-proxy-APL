package reverse.proxy.apl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import reverse.proxy.apl.dto.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String>{

}
