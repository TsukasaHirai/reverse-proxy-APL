package reverse.proxy.apl.controller;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import reverse.proxy.apl.form.LoginUser;

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
	 * @param username formで受信したユーザー名
	 * @param result バリデーション結果
	 * @return
	 */
	@PostMapping("/login")
	public String login(@RequestHeader(name = "dn", required = false) String dn, @ModelAttribute @Validated LoginUser loginUser, BindingResult result) {
		if (result.hasErrors()) {
			LOG.log(Level.INFO, "バリデーションエラー:" + loginUser.toString());
			return "login";
		}
		
		LOG.log(Level.INFO, "DN情報：" +  dn + "ユーザー名：" + loginUser.getUsername());
		if (isEqualsUsernameAndCommonName(dn, loginUser.getUsername())) {
			LOG.log(Level.INFO, "証明書情報とユーザー名が一致しました");
		}

		return "forward:/auth";
	}

	
	/**
	 * ユーザ名とCommon Nameが一致するかを検証する
	 * 
	 * @param dn dn情報
	 * @param username ユーザー名
	 * @return trueであれば common nameとdn情報が一致、一致しなければfalse
	 */
	private boolean isEqualsUsernameAndCommonName(final String dn, final String username) {
		if (dn == null || username == null) {
			return false;
		}
		String[] subjects = StringUtils.split(dn, ",");
		String commonName = Arrays.stream(subjects).filter(subject -> subject.trim().startsWith("CN=")).findFirst().orElse("").trim();
		
		return commonName.substring(3).equals(username);
	}
}
