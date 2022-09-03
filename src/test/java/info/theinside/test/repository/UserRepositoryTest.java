package info.theinside.test.repository;

import info.theinside.test.model.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    private final User user1 = new User();
    private final User user2 = new User();

    @BeforeEach
    public void preparation() {
        user1.setName("User1");
        user1.setPassword("test1");
        user2.setName("User2");
        user2.setPassword("User2");
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.flush();
    }

    @Test
    public void test1_tryFindUserByName() {
        User user = userRepository.findUsersByName(user1.getName()).get();
        Assertions.assertEquals(user1, user);
        Assertions.assertEquals(1L, user.getId());
        user = userRepository.findUsersByName(user2.getName()).get();
        Assertions.assertEquals(user2, user);
        Assertions.assertEquals(2L, user.getId());
        Assertions.assertFalse(userRepository.findUsersByName("User3").isPresent());
    }

    @Test
    public void test2_tryFindUserByNameAndPassword() {
        User user = userRepository.findUsersByNameAndPassword(user1.getName(), user1.getPassword()).get();
        Assertions.assertEquals(user1, user);
        Assertions.assertEquals(1L, user.getId());
        user = userRepository.findUsersByNameAndPassword(user2.getName(), user2.getPassword()).get();
        Assertions.assertEquals(user2, user);
        Assertions.assertEquals(2L, user.getId());
        Assertions.assertFalse(userRepository.findUsersByNameAndPassword(
                        user1.getName(),
                        user2.getPassword()
                ).isPresent()
        );
    }
}