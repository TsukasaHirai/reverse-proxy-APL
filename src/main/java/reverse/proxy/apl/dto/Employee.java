package reverse.proxy.apl.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class Employee {
    
	@Id
    @Size(min = 2, max = 20)
    private String username;
    
    @Size(min = 4, max = 255)
    private String password;
}
