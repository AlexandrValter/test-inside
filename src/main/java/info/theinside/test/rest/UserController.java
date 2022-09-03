package info.theinside.test.rest;

import info.theinside.test.model.User;
import info.theinside.test.model.dto.TokenDto;
import info.theinside.test.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Эндпоинт для добавления нового пользователя. Валидирует пользователя из тела запроса.
    //В ответ в случае успешного добавления отправляет сгенерированный JWT Токен
    @PostMapping("/add")
    public TokenDto createUser(@RequestBody @Valid User user) {
        return userService.createUser(user);
    }

    //Эндпоинт для аутентификации пользователя. В случае если имя пользователя и пароль соовтетствует хранящимся в БД,
    //возвращает в ответ сгенерированный JWT токен
    @PostMapping("/auth")
    public TokenDto getToken(@RequestBody @Valid User user) {
        return userService.getToken(user);
    }
}