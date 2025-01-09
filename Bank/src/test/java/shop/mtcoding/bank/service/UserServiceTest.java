package shop.mtcoding.bank.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserEnum;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.handler.ex.CustomApiException;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    @Mock //@Mock: UserRepository와 BCryptPasswordEncoder를 Mock 객체로 생성합니다. 이 객체들은 실제 DB나 암호화 기능을 사용하지 않고, 테스트를 위한 가짜 객체로 동작합니다
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks //@InjectMocks: userService에 userRepository와 passwordEncoder Mock 객체를 주입하여 UserService 인스턴스를 생성
    private UserService userService;

    @Before
    public void setUp() { //@Before: 테스트 메서드가 실행되기 전에 실행되는 메서드입니다. 여기서는 MockitoAnnotations.initMocks(this)를 호출하여 @Mock 어노테이션이 적용된 객체들을 초기화
        MockitoAnnotations.initMocks(this); //Mock 객체 초기화
    }

    @Test
    public void 회원가입_성공() {
        //Given
        UserService.JoinReqDto joinReqDto = new UserService.JoinReqDto(); //UserService.JoinReqDto 객체는 회원가입 요청을 담기 위한 DTO 여기다 테스트로 담음
        joinReqDto.setUsername("testuser");
        joinReqDto.setPassword("password");
        joinReqDto.setEmail("test@example.com");
        joinReqDto.setFullname("Test User");

        User savedUser = User.builder() //savedUser: 이 객체는 실제로 데이터베이스에 저장될 유저 객체를 나타냅니다. id는 1L, username은 "testuser"로 설정하고, password는 이미 인코딩된 "encodedPassword"로 설정되어 있습니다. 회원가입 시 실제로 저장될 유저 데이터를 모의한 것입니다.
       // UserEnum.CUSTOMER는 role 필드로, 회원가입할 때 고객 역할을 부여합니다.
                .id(1L)
                .username("testuser")
                .password("encodedPassword")
                .email("test@example.com")
                .fullname("Test User")
                .role(UserEnum.CUSTOMER)
                .build();
     //when(...): 이 부분은 Mockito를 사용하여 mock 객체의 행동을 정의하는 부분입니다.
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty()); //when(userRepository.findByUsername("testuser"))는 userRepository의 findByUsername 메서드가 호출될 때, 반환값을 Optional.empty()로 설정합니다. 즉, "testuser"라는 유저 이름을 가진 유저가 이미 존재하지 않음을 의미합니다.
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword"); //when(passwordEncoder.encode("password"))는 비밀번호 "password"를 인코딩할 때, 결과로 "encodedPassword"를 반환하도록 설정합니다. 실제로는 BCryptPasswordEncoder가 비밀번호를 암호화하는 역할을 합니다.
        when(userRepository.save(any(User.class))).thenReturn(savedUser); //when(userRepository.save(any(User.class)))는 userRepository.save() 메서드가 호출되면 savedUser를 반환하도록 설정합니다. 즉, 실제로 User 객체가 데이터베이스에 저장될 때 이 값이 반환됩니다.

        //When
        UserService.JoinRespDto response = userService.회원가입(joinReqDto); //이제 준비된 joinReqDto를 이용해 userService.회원가입() 메서드를 호출하여 회원가입 처리를 시작합니다. 이때 joinReqDto는 회원가입에 필요한 데이터를 담고 있습니다.
        //회원가입 메서드는 UserRepository와 BCryptPasswordEncoder를 사용하므로, 이들의 mock 동작에 따라 유저 데이터가 저장되고, 최종적으로 JoinRespDto가 반환됩니다.

        //then
        assertNotNull(response); //assertNotNull(response): 응답 객체인 response가 null이 아님을 검증합니다. 회원가입이 성공적으로 처리되어 응답이 있어야 합니다.
        assertEquals(Long.valueOf(1L), response.getId()); //assertEquals(Long.valueOf(1L), response.getId()): response 객체의 id가 1L과 동일한지 검증합니다. 이는 savedUser의 id 값으로 설정된 1L입니다.
        assertEquals("testuser", response.getUsername()); //assertEquals("testuser", response.getUsername()): response 객체의 username이 "testuser"와 일치하는지 검증합니다.
        assertEquals("Test User", response.getFullname()); //assertEquals("Test User", response.getFullname()): response 객체의 fullname이 "Test User"와 일치하는지 검증합니다.

        verify(userRepository, times(1)).findByUsername("testuser"); //userRepository.findByUsername("testuser") 메서드가 한 번 호출되었는지 검증합니다. 회원가입 요청이 들어오면, 우선 유저 이름이 중복되는지 확인하기 위해 findByUsername이 호출됩니다.
        verify(userRepository, times(1)).save(any(User.class)); //verify(userRepository, times(1)): userRepository.save(any(User.class)) 메서드가 한 번 호출되었는지 검증합니다. 회원가입이 성공하면 유저 데이터가 데이터베이스에 저장되므로, save 메서드가 한 번 호출되어야 합니다.

    }

    @Test
    public void 회원가입_실패_이미존재하는_유저네임() {
        // Given
        UserService.JoinReqDto joinReqDto = new UserService.JoinReqDto();
        joinReqDto.setUsername("existinguser");
        joinReqDto.setPassword("password");
        joinReqDto.setEmail("test@example.com");
        joinReqDto.setFullname("Test User");

        User existingUser = User.builder()
                .id(1L)
                .username("existinguser")
                .password("encodedPassword")
                .email("existing@example.com")
                .fullname("Existing User")
                .role(UserEnum.CUSTOMER)
                .build();
      //when(userRepository.findByUsername("existinguser")): userRepository.findByUsername이 호출되면, Optional.of(existingUser)가 반환되도록 설정합니다. 즉, "existinguser"라는 유저네임을 가진 유저가 이미 존재함을 의미합니다.
        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(existingUser));

        // When & Then
        try {
            userService.회원가입(joinReqDto);	;
            fail("Expected CustomApiException to be thrown");
        } catch (CustomApiException e) {
            assertEquals("이미 존재하는 유저네임입니다.", e.getMessage());
        }
       //verify(userRepository, times(1)): userRepository.findByUsername("existinguser")가 정확히 한 번 호출되었는지 확인합니다. 이미 존재하는 유저네임을 찾으려는 시도가 있었으므로, 이 메서드는 반드시 한 번 호출됩니다.
        //verify(userRepository, never()).save(any(User.class)): userRepository.save()는 호출되지 않아야 합니다. 왜냐하면 유저네임이 중복되었기 때문에 회원가입이 진행되지 않고 예외가 발생해야 하기 때문입니다. 따라서 save 메서드는 호출되지 않았음을 검증합니다.
        verify(userRepository, times(1)).findByUsername("existinguser");
        verify(userRepository, never()).save(any(User.class));
    }

}
