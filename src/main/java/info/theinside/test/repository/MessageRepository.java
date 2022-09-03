package info.theinside.test.repository;

import info.theinside.test.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "select * from messages as m " +
            "where m.user_id = ?1 " +
            "order by m.created desc " +
            "limit ?2",
            nativeQuery = true)
    Collection<Message> getHistoryMessages(Long userId, int limit);
}