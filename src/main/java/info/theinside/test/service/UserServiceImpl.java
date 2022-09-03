package info.theinside.test.service;

import info.theinside.test.exception.FailedAuthenticationException;
import info.theinside.test.exception.UsernameAlreadyExistException;
import info.theinside.test.jwt.JwtTokenProvider;
import info.theinside.test.model.User;
import info.theinside.test.model.dto.TokenDto;
import info.theinside.test.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public UserServiceImpl(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //Метод для добавления нового пользователя в БД
    @Override
    public TokenDto createUser(User user) {
        //проверяем на наличие пользователя с таким именем в БД
        if (userRepository.findUsersByName(user.getName()).isPresent()) {
            //если в БД нашелся пользователь с таким именем, выбрасываем исключение
            throw new UsernameAlreadyExistException(
                    String.format("Please select a different name, '%s' is already used",
                            user.getName())
            );
        } else {
            //если в БД нет такого пользователя - добавляем его и возвращаем сгенерированный токен
            userRepository.save(user);
            log.info("New user added: {}", user.getName());
            return new TokenDto(jwtTokenProvider.createToken(user.getName()));
        }
    }

    //Метод генерации токена (аутентификации пользователя)
    @Override
    public TokenDto getToken(User user) {
        //проверяем наличие в БД пользователя с таким именем и паролем
        if (userRepository.findUsersByNameAndPassword(user.getName(), user.getPassword()).isPresent()) {
            //если пользователь найден - возвращаем сгенерированный токен
            log.info("User authentication: {}", user.getName());
            return new TokenDto(jwtTokenProvider.createToken(user.getName()));
        } else {
            //если такого пользователя нет, выбрасывается исключение с просьбой проверки корректности ввода данных
            throw new FailedAuthenticationException("Please check your name or password and try again");
        }
    }
}