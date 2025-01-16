package shop.mtcoding.bank.dto.user;

import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.util.CustomDateUtil;

public class UserRespDto {

    @Setter
    @Getter
    public static class LoginRespDto {
        private Long id;
        private String username;
        private String createdAt;

        public LoginRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.createdAt = CustomDateUtil.toStringFormat(user.getCreatedAt());
        }
    }
    @Getter
    @Setter
    public static class JoinRespDto { //회원가입 한것을 내보낼떄 쓰는 엔티티
        private Long id;
        private String username;
        private String fullname;

        public JoinRespDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.fullname = user.getFullname();
        }
    }
}
