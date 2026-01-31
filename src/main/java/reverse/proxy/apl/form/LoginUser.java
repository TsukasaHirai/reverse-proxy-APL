package reverse.proxy.apl.form;

import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class LoginUser {

	@Size(min = 2, max = 20)
	private String username;

	@Size(min = 6, max = 20)
	private String password;
}
