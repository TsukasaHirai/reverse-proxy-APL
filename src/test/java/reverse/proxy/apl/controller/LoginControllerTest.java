package reverse.proxy.apl.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import reverse.proxy.apl.confing.DataLoader;

@SpringBootTest
@AutoConfigureMockMvc
class LoginControllerTest {

    @MockitoBean
    private DataLoader dataLoader;

    @Autowired
    MockMvc mockMvc;
    
    @DisplayName("ログインせずにアクセスできるかのテスト")
	@ParameterizedTest
	@ValueSource(strings = {"/", "/home"})
    void permitAllTest(String uri) throws Exception {
    	
    	mockMvc.perform(
    			get(uri)
    			)
    	        .andExpectAll(
    	        		status().isOk(),
    	        		view().name("home"));
    }
    
	@ParameterizedTest
	@ValueSource(strings = {"/hello", "/xxxx"})
    void noPermitAllTest(String uri) throws Exception {
    	
    	mockMvc.perform(
    			get(uri)
    			)
    	        .andExpectAll(
    	        		status().isFound());
    }
}
