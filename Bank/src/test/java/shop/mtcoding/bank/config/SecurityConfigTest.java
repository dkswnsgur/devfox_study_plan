package shop.mtcoding.bank.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    // 1. 인증 없이 접근 가능한 URL 테스트
    @Test
    public void permitAllAccessTest() throws Exception {
        mockMvc.perform(get("/")) // 기본적으로 모든 사용자가 접근 가능
                .andExpect(status().isOk());
    }

    // 2. 인증이 필요한 URL 테스트 (비인증 사용자로 접근)
    @Test
    public void unauthenticatedAccessTest() throws Exception {
        mockMvc.perform(get("/api/s/secure-endpoint")) // 인증이 필요한 URL
                .andExpect(status().isUnauthorized()); // 401 상태 코드 반환
    }

    // 3. 인증된 사용자로 접근 테스트
    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "user", roles = "USER")
    public void authenticatedAccessTest() throws Exception {
        mockMvc.perform(get("/api/s/secure-endpoint")) // 인증이 필요한 URL
                .andExpect(status().isOk());
    }

    // 4. 관리자 권한이 필요한 URL 테스트
    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "admin", roles = "ADMIN")
    public void adminAccessTest() throws Exception {
        mockMvc.perform(get("/api/admin/secure-endpoint")) // 관리자 권한이 필요한 URL
                .andExpect(status().isOk());
    }

    // 5. 관리자 권한이 없는 사용자로 관리자 URL 접근 테스트
    @Test
    @org.springframework.security.test.context.support.WithMockUser(username = "user", roles = "USER")
    public void forbiddenAccessTest() throws Exception {
        mockMvc.perform(get("/api/admin/secure-endpoint")) // 관리자 권한이 필요한 URL
                .andExpect(status().isForbidden()); // 403 상태 코드 반환
    }
}
