package info.theinside.test.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.theinside.test.model.User;
import info.theinside.test.model.dto.TokenDto;
import info.theinside.test.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController controller;
    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();
    private final TokenDto token = new TokenDto("Test.token.dto");
    private final User user = new User();

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
        user.setId(1L);
        user.setName("User");
        user.setPassword("Password");
    }

    @Test
    public void test1_tryCreateUserWhenUserIsValid() {
        when(userService.createUser(Mockito.any(User.class)))
                .thenReturn(token);
        try {
            mvc.perform(post("/user/add")
                            .content(mapper.writeValueAsString(user))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(token)));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void test2_tryCreateUserWhenUserNameIsNotValid() {
        try {
            user.setName("");
            mvc.perform(post("/user/add")
                            .content(mapper.writeValueAsString(user))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(400));
            user.setName(null);
            mvc.perform(post("/user/add")
                            .content(mapper.writeValueAsString(user))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(400));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void test3_tryCreateUserWhenUserPasswordIsNotValid() {
        try {
            user.setPassword("");
            mvc.perform(post("/user/add")
                            .content(mapper.writeValueAsString(user))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(400));
            user.setPassword(null);
            mvc.perform(post("/user/add")
                            .content(mapper.writeValueAsString(user))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(400));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void test4_tryGetTokenWhenUserIsValid() {
        when(userService.getToken(Mockito.any(User.class)))
                .thenReturn(token);
        try {
            mvc.perform(post("/user/auth")
                            .content(mapper.writeValueAsString(user))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(mapper.writeValueAsString(token)));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void test5_tryGetTokenWhenUserNameIsNotValid() {
        try {
            user.setName("");
            mvc.perform(post("/user/auth")
                            .content(mapper.writeValueAsString(user))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(400));
            user.setName(null);
            mvc.perform(post("/user/auth")
                            .content(mapper.writeValueAsString(user))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(400));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void test6_tryGetTokenWhenUserPasswordIsNotValid() {
        try {
            user.setPassword("");
            mvc.perform(post("/user/auth")
                            .content(mapper.writeValueAsString(user))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(400));
            user.setPassword(null);
            mvc.perform(post("/user/auth")
                            .content(mapper.writeValueAsString(user))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(400));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}