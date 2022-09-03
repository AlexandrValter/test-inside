package info.theinside.test.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//Dto-объект, передаваемый в классе UserController в случае успешной аутентификации пользователя
//В объекте зашифрован JWT токен
public class TokenDto {
    private String token;
}