package reverse.proxy.apl.util;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

public final class LoginUtil {
	
	private LoginUtil() {
		
	}
	
	/**
	 * ユーザ名とCommon Nameが一致するかを検証する
	 * 
	 * @param dn dn情報
	 * @param username ユーザー名
	 * @return trueであれば common nameとdn情報が一致、一致しなければfalse
	 */
	public static boolean isEqualsUsernameAndCommonName(final String dn, final String username) {
		if (StringUtils.isAnyEmpty(dn, username)) {
			return false;
		}
		String[] subjects = dn.split(",");

		String commonName = Arrays.stream(subjects).filter(subject -> subject.trim().startsWith("CN=")).findFirst().orElse("").trim();
		
		return commonName.substring(3).equals(username);
	}
}
