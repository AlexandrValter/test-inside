package info.theinside.test.repository;

import info.theinside.test.model.Message;
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

import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DataJpaTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MessageRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    private final User user1 = new User();
    private final User user2 = new User();
    private final Message message1 = new Message();
    private final Message message2 = new Message();
    private final Message message3 = new Message();

    @BeforeEach
    public void preparation() {
        user1.setName("User1");
        user1.setPassword("test1");
        user2.setName("User2");
        user2.setPassword("User2");
        message1.setUser(user1);
        message2.setUser(user2);
        message3.setUser(user2);
        message1.setMessage("Test message 1");
        message2.setMessage("Test message 2");
        message3.setMessage("Test message 3");
        message1.setCreated(LocalDateTime.now());
        message2.setCreated(LocalDateTime.now().plusMinutes(2));
        message3.setCreated(LocalDateTime.now().plusMinutes(5));
        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(message1);
        entityManager.persist(message2);
        entityManager.persist(message3);
        entityManager.flush();
    }

    @Test
    public void test1_tryGetHistoryMessages() {
        Query nativeQuery = entityManager.getEntityManager()
                .createNativeQuery("select * from messages as m " +
                        "where m.user_id = :userId " +
                        "order by m.created desc " +
                        "limit :limit", Message.class);
        nativeQuery.setParameter("userId", user2.getId());
        nativeQuery.setParameter("limit", 5);
        List<Message> result = nativeQuery.getResultList();
        List<Message> expectedList = Stream.of(message2, message3)
                .sorted(Comparator.comparing(Message::getCreated).reversed())
                .collect(Collectors.toList());
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(expectedList, result);
    }
}