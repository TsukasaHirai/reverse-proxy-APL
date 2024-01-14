package reverse.proxy.apl.controller;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController {
	
	Logger logger = Logger.getLogger(LoginController.class.getName());

	@GetMapping({"/", "/home"})
	public String index() {
		return "home";
	}
	
	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@PostMapping("/login")
	public String login(HttpServletRequest httpRequest, @ModelAttribute("username") @Validated String username, BindingResult result) {
		logger.log(Level.INFO, "DN情報：" +  httpRequest.getHeader("dn") + "ユーザー名：" + username);
		if (Objects.equals(httpRequest.getHeader("dn"), username)) {
			return "forward:/auth";
		}
		System.out.println("証明書情報とユーザー名が一致しません");
		return "login";
	}
}
