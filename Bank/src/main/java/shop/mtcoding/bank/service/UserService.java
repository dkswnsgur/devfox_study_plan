package shop.mtcoding.bank.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserEnum;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.handler.ex.CustomApiException;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {
    //생성자를 통해 주입된다. Spring에서는 이 방식을 생성자 주입(Constructor Injection)이라고 한다
    private final UserRepository userRepository; // 데이터베이스에서 사용자 정보를 조회하고 저장하는 리포지토리입니다.
    private final BCryptPasswordEncoder passwordEncoder; //비밀번호를 안전하게 인코딩하기 위한 인코더입니다. 비밀번호는 평문으로 저장하지 않고 암호화하여 저장합니다.

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 서비스는 DTO를 요청 받고 DTO로 응답한다
    @Transactional //트랜잭션이 메서드 시작일떄 시작되고 종료될떄 함꼐 종료
    public JoinRespDto 회원가입(JoinReqDto joinReqDto) {
        // 1. 동일 유지 네임 존재 검사
        Optional<User> userOP = userRepository.findByUsername(joinReqDto.getUsername()); //findByUsername(joinReqDto.getUsername())를 사용하여, 이미 동일한 유저네임이 존재하는지 확인합니다.
        if(userOP.isPresent()) { //만약 이미 존재하는 유저네임이 있으면, CustomApiException 예외를 던져 사용자에게 오류 메시지를 전달합니다.
            throw new CustomApiException("이미 존재하는 유저네임입니다.", 400);
        }

        // 2. 패스워드 인코딩 + 회원가입
        User userPS = userRepository.save(joinReqDto.toEntity(passwordEncoder)); //joinReqDto.toEntity(passwordEncoder)는 JoinReqDto에서 제공된 정보를 기반으로 User 엔티티를 생성합니다.
       //toEntity 메서드는 JoinReqDto에서 User 엔티티를 생성하는 로직을 담고 있어야 합니다. 이때 passwordEncoder를 사용하여 비밀번호를 인코딩한 후 저장합니다.
        //userRepository.save()를 호출하여 사용자를 데이터베이스에 저장합니다.

        // 3. dto 응답
        return new JoinRespDto(userPS); //JoinRespDto는 회원가입 성공 후 응답할 DTO입니다. userPS는 데이터베이스에 저장된 User 엔티티 객체로, 이를 JoinRespDto에 전달하여 응답 데이터를 생성합니다.

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

    @Getter
    @Setter
    public static class JoinReqDto { //회원가입을 받을떄 쓰는 엔티티
        //유효성 검사
        private String username;
        private String password;
        private String email;
        private String fullname;

        public User toEntity(BCryptPasswordEncoder passwordEncoder) {
            return User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .fullname(fullname)
                    .role(UserEnum.CUSTOMER)
                    .build();
        }
    }
}
