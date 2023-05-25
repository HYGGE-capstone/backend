package hygge.backend.repository;

import hygge.backend.entity.Member;
import hygge.backend.entity.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRoomRepository extends JpaRepository<MessageRoom, Long> {
    Optional<MessageRoom> findByFromAndTo(Member from, Member to);
}
