package info.theinside.test.service;

import info.theinside.test.jwt.JwtTokenProvider;
import info.theinside.test.model.User;
import info.theinside.test.model.dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final User user1 = new User();
    private final User user2 = new User();

    @BeforeEach
    public void restartIdentity() {
        user1.setName("User1");
        user2.setName("User2");
        user1.setPassword("1234");
        user2.setPassword("1234");
    }

    @Test
    public void test1_createUser() {
        TokenDto tokenDto = userService.createUser(user1);
        boolean isValid = jwtTokenProvider.validateToken(user1.getName(), tokenDto.getToken());
        assertTrue(isValid);
    }

    @Test
    public void test2_getToken() {
        userService.createUser(user2);
        TokenDto tokenDto = userService.getToken(user2);
        boolean isValid = jwtTokenProvider.validateToken(user2.getName(), tokenDto.getToken());
        assertTrue(isValid);
    }
}