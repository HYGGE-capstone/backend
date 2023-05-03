package hygge.backend.repository;

import hygge.backend.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    boolean existsByEmailForm(String emailForm);
}
