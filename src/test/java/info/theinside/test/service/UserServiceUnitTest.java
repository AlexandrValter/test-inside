package info.theinside.test.service;

import info.theinside.test.exception.FailedAuthenticationException;
import info.theinside.test.exception.UsernameAlreadyExistException;
import info.theinside.test.jwt.JwtTokenProvider;
import info.theinside.test.model.User;
import info.theinside.test.model.dto.TokenDto;
import info.theinside.test.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private UserServiceImpl service;
    private final User user = new User(1L, "User1", "1234");
    private final String token = "test.jwt.token";

    @Test
    public void test1_tryAddUser() {
        Mockito
                .when(userRepository.findUsersByName(Mockito.anyString()))
                .thenReturn(Optional.empty());
        Mockito
                .when(userRepository.save(Mockito.any(User.class)))
                .thenReturn(user);
        Mockito
                .when(jwtTokenProvider.createToken(Mockito.anyString()))
                .thenReturn(token);
        TokenDto tokenDto = service.createUser(user);
        Assertions.assertEquals(new TokenDto(token), tokenDto);
    }

    @Test
    public void test2_tryAddUserWhenUserNameIsAlreadyExist() {
        Mockito
                .when(userRepository.findUsersByName(Mockito.anyString()))
                .thenReturn(Optional.of(user));
        UsernameAlreadyExistException thrown = Assertions.assertThrows(UsernameAlreadyExistException.class, () ->
                service.createUser(user));
        Assertions.assertEquals("Please select a different name, 'User1' is already used", thrown.getMessage());
    }

    @Test
    public void test3_tryGetToken() {
        Mockito
                .when(userRepository.findUsersByNameAndPassword(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(jwtTokenProvider.createToken(Mockito.anyString()))
                .thenReturn(token);
        TokenDto tokenDto = service.getToken(user);
        Assertions.assertEquals(new TokenDto(token), tokenDto);
    }

    @Test
    public void test4_tryGetTokenWhenUsernameOrPasswordIsIncorrect() {
        Mockito
                .when(userRepository.findUsersByNameAndPassword(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.empty());
        FailedAuthenticationException thrown = Assertions.assertThrows(FailedAuthenticationException.class, () ->
                service.getToken(user));
        Assertions.assertEquals("Please check your name or password and try again", thrown.getMessage());
    }
}