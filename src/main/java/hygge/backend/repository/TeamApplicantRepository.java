package hygge.backend.repository;

import hygge.backend.entity.TeamApplicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamApplicantRepository extends JpaRepository<TeamApplicant, Long> {
}
