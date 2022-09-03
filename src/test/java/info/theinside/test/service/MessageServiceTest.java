package info.theinside.test.service;

import info.theinside.test.exception.UsernameAlreadyExistException;
import info.theinside.test.model.User;
import info.theinside.test.model.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MessageServiceTest {
    private final UserService userService;
    private final MessageService messageService;
    private final User user1 = new User();
    private final User user2 = new User();
    private String token1;
    private String token2;


    @BeforeEach
    public void restartIdentity() {
        user1.setName("User");
        user2.setName("New user");
        user1.setPassword("1234");
        user2.setPassword("12344321");
        try {
            token1 = userService.createUser(user1).getToken();
            token2 = userService.createUser(user2).getToken();
        } catch (UsernameAlreadyExistException e) {
            token1 = userService.getToken(user1).getToken();
            token2 = userService.getToken(user2).getToken();
        }
    }

    @Test
    public void test1_addMessage() {
        MessageDto messageDto = new MessageDto(user1.getName(), "test");
        String bearer = "Bearer_".concat(token1);
        Collection<MessageDto> result = messageService.addMessage(messageDto, bearer);
        assertThat(result, equalTo(Collections.emptyList()));
    }

    @Test
    public void test2_getHistory() {
        MessageDto messageDto = new MessageDto(user2.getName(), "test");
        String bearer = "Bearer_".concat(token2);
        messageService.addMessage(messageDto, bearer);
        MessageDto history = new MessageDto(user2.getName(), "history 5");
        Collection<MessageDto> result = messageService.addMessage(history, bearer);
        assertThat(result.size(), equalTo(1));
        assertThat(List.copyOf(result).get(0).getName(), equalTo(messageDto.getName()));
        assertThat(List.copyOf(result).get(0).getMessage(), equalTo(messageDto.getMessage()));
    }
}