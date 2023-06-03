package hygge.backend.repository;

import hygge.backend.entity.MemberTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberTeamRepository extends JpaRepository<MemberTeam, Long> {
    List<MemberTeam> findByMemberId(Long memberId);

    List<MemberTeam> findByTeamId(Long teamId);

    boolean existsByMemberIdAndTeamId(Long memberId, Long teamId);
    Optional<MemberTeam> findByMemberIdAndTeamId(Long memberId, Long teamId);
}
