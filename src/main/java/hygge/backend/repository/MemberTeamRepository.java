package hygge.backend.repository;

import hygge.backend.entity.MemberTeam;
import hygge.backend.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {
    List<MemberTeam> findByMemberId(Long memberId);
}
