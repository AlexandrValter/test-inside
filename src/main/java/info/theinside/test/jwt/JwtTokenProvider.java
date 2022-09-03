package info.theinside.test.jwt;

import info.theinside.test.exception.FailedAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {
    //Секретное слово и срок действия токена прописаны в application.properties
    @Value("${jwt.token.secret}")
    private String secret;
    @Value("${jwt.token.expired}")
    private Long validTime;

    public String createToken(String name) {
        Claims claims = Jwts.claims();
        claims.put("name", name);
        Instant validity = Instant.now().plusMillis(validTime);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(validity))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    //  Метод проверяет токен: сравнивает имя пользователя с именем, закодированным в токене.
    //  В случае успешной проверки возвращает true
    public boolean validateToken(String name, String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getOrDefault("name", null)
                    .equals(name);
        } catch (ExpiredJwtException e) {
            //Исключение выбрасывается в случае завершения срока действия токена
            throw new FailedAuthenticationException("Token expiration time has expired");
        }
    }
}