package hygge.backend.repository;

import hygge.backend.entity.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
    boolean existsByMemberIdAndSubjectId(Long memberId, Long subjectId);

    Optional<Subscribe> findByMemberIdAndSubjectId(Long memberId, Long subjectId);

    List<Subscribe> findByMemberId(Long memberId);

    List<Subscribe> findBySubjectId(Long subjectId);
}
