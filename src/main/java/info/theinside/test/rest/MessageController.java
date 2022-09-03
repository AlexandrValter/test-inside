package info.theinside.test.rest;

import info.theinside.test.model.dto.MessageDto;
import info.theinside.test.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(path = "/message")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    //Post-метод валидирует принимаемый в теле запроса объект MessageDto, "вытаскивает" строку с токеном из заголовка
    @PostMapping
    public Collection<MessageDto> addMessage(@Valid @RequestBody MessageDto messageDto,
                                             @RequestHeader("Authorization") String bearer) {
        return messageService.addMessage(messageDto, bearer);
    }
}