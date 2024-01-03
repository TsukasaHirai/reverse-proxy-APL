package reverse.proxy.apl.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import reverse.proxy.apl.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String>{

}
