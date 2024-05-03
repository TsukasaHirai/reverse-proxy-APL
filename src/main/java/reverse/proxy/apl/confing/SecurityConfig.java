package reverse.proxy.apl.confing;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

/**
 * ログイン設定。
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	/**
	 * パスワードの暗号化方式設定。
	 * Spring Securityを使用する上では {@link PasswordEncoder}は必須。
	 *
	 * @return {@link BCryptPasswordEncoder}
	 * @see <a href="https://spring.pleiades.io/spring-security/reference/servlet/authentication/passwords/dao-authentication-provider.html">DaoAuthenticationProvider</a>
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * ログイン、ログアウト設定を行う。
	 *
	 * @param http 設定する{@link HttpSecurity}
	 * @return 設定した{@link HttpSecurity}
	 * @throws Exception
	 */
	@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(requests -> requests
				.requestMatchers("/", "/home", "/actuator/*").permitAll()
				.anyRequest().authenticated()
			)
			.formLogin(form -> form
				.loginPage("/login")
				.loginProcessingUrl("/auth")
				.permitAll()
			)
			.logout(LogoutConfigurer::permitAll);
		return http.build();
	}
}
