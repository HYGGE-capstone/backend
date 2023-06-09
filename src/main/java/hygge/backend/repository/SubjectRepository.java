package hygge.backend.repository;

import hygge.backend.entity.School;
import hygge.backend.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(String name, String code);

    List<Subject> findBySchool(School school);
}
