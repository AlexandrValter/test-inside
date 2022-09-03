package info.theinside.test.service;

import info.theinside.test.exception.BearerTokenIsNotFound;
import info.theinside.test.exception.FailedAuthenticationException;
import info.theinside.test.jwt.JwtTokenProvider;
import info.theinside.test.model.Message;
import info.theinside.test.model.User;
import info.theinside.test.model.dto.MessageDto;
import info.theinside.test.model.dto.MessageMapper;
import info.theinside.test.repository.MessageRepository;
import info.theinside.test.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class MessageServiceUnitTest {
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private MessageServiceImpl service;
    private final MessageDto messageDto = new MessageDto("User1", "Message for test");
    private final User user = new User(1L, "User1", "1234");
    private final String bearer = "Bearer_test.jwt.token";
    private final MessageDto history = new MessageDto("User1", "history 2");

    @Test
    public void test1_tryAddMessage() {
        Mockito
                .when(jwtTokenProvider.validateToken(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);
        Mockito
                .when(userRepository.findUsersByName(Mockito.anyString()))
                .thenReturn(Optional.of(user));
        Mockito
                .when(messageRepository.save(Mockito.any(Message.class)))
                .thenReturn(MessageMapper.toMessage(messageDto));
        Collection<MessageDto> list = service.addMessage(messageDto, bearer);
        Assertions.assertEquals(Collections.emptyList(), list);
    }

    @Test
    public void test2_tryAddMessageWithEmptyBearer() {
        String emptyBearer = "";
        BearerTokenIsNotFound thrown = Assertions.assertThrows(BearerTokenIsNotFound.class, () ->
                service.addMessage(messageDto, emptyBearer));
        Assertions.assertEquals("Bearer token is not found in header request", thrown.getMessage());
    }

    @Test
    public void test3_tryAddMessageWithIncorrectBearer() {
        String incorrectBearer = "Br_test.jwt.token";
        BearerTokenIsNotFound thrown = Assertions.assertThrows(BearerTokenIsNotFound.class, () ->
                service.addMessage(messageDto, incorrectBearer));
        Assertions.assertEquals("Bearer token is not found in header request", thrown.getMessage());
    }

    @Test
    public void test4_tryAddMessageWhenTokenIsNotValid() {
        Mockito
                .when(jwtTokenProvider.validateToken(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(false);
        FailedAuthenticationException thrown = Assertions.assertThrows(FailedAuthenticationException.class, () ->
                service.addMessage(messageDto, bearer));
        Assertions.assertEquals("Authentication error: please enter your name and password again", thrown.getMessage());
    }

    @Test
    public void test5_tryGetHistory() {
        Mockito
                .when(jwtTokenProvider.validateToken(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);
        Mockito
                .when(userRepository.findUsersByName(Mockito.anyString()))
                .thenReturn(Optional.of(user));
        Message message = MessageMapper.toMessage(messageDto);
        message.setUser(user);
        Mockito
                .when(messageRepository.getHistoryMessages(Mockito.anyLong(), Mockito.anyInt()))
                .thenReturn(List.of(message));
        Collection<MessageDto> list = service.addMessage(history, bearer);
        Assertions.assertEquals(List.of(messageDto), list);
    }

    @Test
    public void test6_tryGetHistoryWhenMessageHistoryWithError() {
        Mockito
                .when(jwtTokenProvider.validateToken(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);
        Mockito
                .when(userRepository.findUsersByName(Mockito.anyString()))
                .thenReturn(Optional.of(user));
        history.setMessage("history sd2");
        Mockito
                .when(messageRepository.save(Mockito.any(Message.class)))
                .thenReturn(MessageMapper.toMessage(messageDto));
        Collection<MessageDto> list = service.addMessage(history, bearer);
        Assertions.assertEquals(Collections.emptyList(), list);
        history.setMessage("history 2 5");
        list = service.addMessage(history, bearer);
        Assertions.assertEquals(Collections.emptyList(), list);
        history.setMessage("history -2");
        list = service.addMessage(history, bearer);
        Assertions.assertEquals(Collections.emptyList(), list);
    }


}