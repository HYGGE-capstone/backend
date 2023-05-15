package hygge.backend.repository;

import hygge.backend.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    boolean existsByMemberIdAndSubjectId(Long memberId, Long subjectId);

}
