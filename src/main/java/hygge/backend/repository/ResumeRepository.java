package hygge.backend.repository;

import hygge.backend.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    boolean existsByMemberIdAndSubjectId(Long memberId, Long subjectId);

    Optional<Resume> findBySubjectIdAndMemberId(Long subjectId, Long memberId);


}
