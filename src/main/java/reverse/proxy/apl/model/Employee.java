package reverse.proxy.apl.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Employee {
    
	@Id
    @Size(min = 2, max = 20)
    private String username;
    
    @Size(min = 4, max = 255)
    private String password;
}
