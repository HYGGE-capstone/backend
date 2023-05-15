package hygge.backend.repository;

import hygge.backend.entity.MemberTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {
    List<MemberTeam> findByMemberId(Long memberId);
}
