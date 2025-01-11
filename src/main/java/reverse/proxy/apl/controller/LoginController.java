package reverse.proxy.apl.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import reverse.proxy.apl.form.LoginUser;
import reverse.proxy.apl.util.LoginUtil;
import reverse.proxy.apl.util.TraceUtil;

@Controller
public class LoginController {
	
	private static final Logger LOG = Logger.getLogger(LoginController.class.getName());

	@GetMapping({"/", "/home"})
	public String index() {
		return "home";
	}
	
	@GetMapping("/hello")
	public String hello() {
		return "hello";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("loginUser", new LoginUser());
		return "login";
	}
	

	/**
	 * ログイン処理
	 * 
	 * @param dn Nginxから受信したクライアント証明書情報
	 * @param loginUser formで受信したユーザー情報
	 * @param result バリデーション結果
	 * @return
	 */
	@PostMapping("/login")
	public String login(@RequestHeader(name = "dn", required = false) String dn, @ModelAttribute @Validated LoginUser loginUser, BindingResult result) {
		Span span = TraceUtil.startSpan("login");
		try (Scope scope = span.makeCurrent()){
			if (result.hasErrors()) {
				LOG.log(Level.INFO, "バリデーションエラー:" + loginUser.toString());
				return "login";
			}
			
			LOG.log(Level.INFO, "DN情報：" +  dn + "ユーザー名：" + loginUser.getUsername());
			if (LoginUtil.isEqualsUsernameAndCommonName(dn, loginUser.getUsername())) {
				LOG.log(Level.INFO, "証明書情報とユーザー名が一致しました");
			}
		} finally {
			span.end();
		}

		return "forward:/auth";
	}
}
