package shop.mtcoding.bank.config.jwt;

import org.junit.jupiter.api.Test;
import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserEnum;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class JwtProcessTest {

    @Test
    public void create_test() throws Exception {
        //given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        LoginUser loginUser = new LoginUser(user);

        //when
        String jwtToken = JwtProcess.create(loginUser);
        System.out.println("테스트 :" + jwtToken);

        //then
        assert(jwtToken.startsWith(JwtVO.TOKEN_PREFIX));
    }

    @Test  //토큰 검증
    public void verify_test() throws Exception {
        //given
        String jwtToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJiYW5rIiwiZXhwIjoxNzM3OTgwNDg5LCJpZCI6MSwicm9sZSI6IkNVU1RPTUVSIn0.9-bcbJGCOYAHw3-d4qDBQueHqIvuQ-s9FUSaO6wJUQ_kR_ufMJyE9OX1eeXmVdTEZspo9P6DKg7U3FdycSZuPQ";

        //when
        LoginUser loginUser = JwtProcess.verify(jwtToken);
        System.out.println("테스트 :" + loginUser.getUser().getId());
        System.out.println("테스트 :" + loginUser.getUser().getRole().name());

        //then
        assertThat(loginUser.getUser().getId()).isEqualTo(1L);
        assertThat(loginUser.getUser().getRole()).isEqualTo(UserEnum.CUSTOMER);
    }


}
