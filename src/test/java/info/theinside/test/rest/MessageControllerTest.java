package info.theinside.test.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import info.theinside.test.model.dto.MessageDto;
import info.theinside.test.service.MessageService;
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
import java.util.Collections;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MessageControllerTest {
    @Mock
    private MessageService messageService;
    @InjectMocks
    private MessageController controller;
    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();
    private final MessageDto messageDto = new MessageDto("User1", "Message for test");

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    public void test1_addMessageWhenMessageDtoIsValid() {
        when(messageService.addMessage(Mockito.any(MessageDto.class), Mockito.anyString()))
                .thenReturn(Collections.emptyList());
        try {
            mvc.perform(post("/message")
                            .content(mapper.writeValueAsString(messageDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "JWT"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", is(empty())));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void test2_addMessageWhenMessageIsNotValid() {
        try {
            messageDto.setMessage("");
            mvc.perform(post("/message")
                            .content(mapper.writeValueAsString(messageDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "JWT"))
                    .andExpect(status().is(400));
            messageDto.setMessage(null);
            mvc.perform(post("/message")
                            .content(mapper.writeValueAsString(messageDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "JWT"))
                    .andExpect(status().is(400));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void test3_addMessageWhenNameIsNotValid() {
        try {
            messageDto.setName("");
            mvc.perform(post("/message")
                            .content(mapper.writeValueAsString(messageDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "JWT"))
                    .andExpect(status().is(400));
            messageDto.setName(null);
            mvc.perform(post("/message")
                            .content(mapper.writeValueAsString(messageDto))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header("Authorization", "JWT"))
                    .andExpect(status().is(400));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}