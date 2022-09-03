package info.theinside.test.service;

import info.theinside.test.exception.BearerTokenIsNotFound;
import info.theinside.test.exception.FailedAuthenticationException;
import info.theinside.test.jwt.JwtTokenProvider;
import info.theinside.test.model.Message;
import info.theinside.test.model.dto.MessageDto;
import info.theinside.test.model.dto.MessageMapper;
import info.theinside.test.repository.MessageRepository;
import info.theinside.test.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    public MessageServiceImpl(
            JwtTokenProvider jwtTokenProvider,
            UserRepository userRepository,
            MessageRepository messageRepository
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public Collection<MessageDto> addMessage(MessageDto messageDto, String bearer) {
        String token;
        if (!bearer.isBlank() && bearer.startsWith("Bearer")) { //проверка строки на соответствие вида: Bearer_ + токен
            token = bearer.substring(7); //убираем лишнее из строки, оставляем только закодированный токен
        } else {
            //В случае неверного формата строки выбрасывается исключение
            throw new BearerTokenIsNotFound("Bearer token is not found in header request");
        }
        //валидируем токен
        if (jwtTokenProvider.validateToken(messageDto.getName(), token)) {
            Message message = MessageMapper.toMessage(messageDto);
            message.setUser(userRepository.findUsersByName(messageDto.getName()).get());
            //проверка на добавление сообщения или запрос истории сообщений:
            if (messageDto.getMessage().startsWith("history")) {
                //если сообщение начинается со слова history, то оно передается в метод getNumberOfMessages
                int number = getNumberOfMessages(messageDto.getMessage());
                //если из метода getNumberOfMessages() вернулось число -1,
                //то считаем что пользователь добавляет сообщение, а не запрашивает историю
                if (number == -1) {
                    messageRepository.save(message);
                    log.info("User '{}' add new message", messageDto.getName());
                    return new ArrayList<>();
                } else {
                    //Если из метода getNumberOfMessages() вернулось число, отличное от -1, то извлекаем из базы данных
                    // количество запрошенных сообщений, отсортированных по дате от новым к более старым
                    // и возвращаем списком
                    log.info("User '{}' requested last {} posts", messageDto.getName(), number);
                    return messageRepository.getHistoryMessages(message.getUser().getId(), number).stream()
                            .map(MessageMapper::toMessageDto)
                            .collect(Collectors.toList());
                }
            } else {
                //если сообщение не начинается со слова history то сразу добавляем его в базу данных
                messageRepository.save(message);
                log.info("User '{}' add new message", messageDto.getName());
                return new ArrayList<>();
            }
        } else {
            //если токен не прошел валидацию, выбрасывается исключение
            throw new FailedAuthenticationException("Authentication error: please enter your name and password again");
        }
    }

    //Данный метод проверяет сообщение на соответствие его формату: "history X", где X - положительное число
    //Если сообщение соответствует этому формату, то в ответ возращается число Х, если нет - возвращается -1
    private int getNumberOfMessages(String message) {
        String[] text = message.split(" ");
        if (text.length == 2) {
            try {
                int number = Integer.parseInt(text[1]);
                if (number > 0) {
                    return number;
                } else {
                    return -1;
                }
            } catch (IllegalArgumentException e) {
                return -1;
            }
        } else {
            return -1;
        }
    }
}