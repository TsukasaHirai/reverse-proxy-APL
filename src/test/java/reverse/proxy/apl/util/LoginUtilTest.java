package reverse.proxy.apl.util;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LoginUtilTest {

    @DisplayName("証明書情報とユーザ名の判定のTrueテスト")
    @ParameterizedTest(name = "Case{index} dn = {0}, username = {1}")
    @MethodSource("isEqualsUsernameAndCommonNameTrueTestValue")
    void isEqualsUsernameAndCommonNameTrueTest(String dn, String username) {
    	
        assertTrue(LoginUtil.isEqualsUsernameAndCommonName(dn, username));
    }

    static Stream<Arguments> isEqualsUsernameAndCommonNameTrueTestValue() {
        return Stream.of(
                Arguments.of("CN=test,test", "test"),
                Arguments.of("CN=test", "test")
        );
    }

    @DisplayName("証明書情報とユーザ名の判定のFalseテスト")
    @ParameterizedTest(name = "Case{index} dn = {0}, username= {1}")
    @MethodSource("isEqualsUsernameAndCommonNameFalseTestValue")
    void isEqualsUsernameAndCommonNameFalseTest(String dn, String username) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        assertFalse(LoginUtil.isEqualsUsernameAndCommonName(dn, username));
    }

    static Stream<Arguments> isEqualsUsernameAndCommonNameFalseTestValue() {
        return Stream.of(
                Arguments.of("", ""),
                Arguments.of(null, null),
                Arguments.of("test", null),
                Arguments.of("CN=user,user", "test")
        );
    }

}
