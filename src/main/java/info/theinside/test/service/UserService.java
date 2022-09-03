package info.theinside.test.service;

import info.theinside.test.model.User;
import info.theinside.test.model.dto.TokenDto;

public interface UserService {
    TokenDto createUser(User user);

    TokenDto getToken(User user);
}
