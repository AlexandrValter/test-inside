package info.theinside.test.repository;

import info.theinside.test.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUsersByName(String name);

    Optional<User> findUsersByNameAndPassword(String name, String password);
}