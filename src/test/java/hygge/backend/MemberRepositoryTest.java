package hygge.backend;

import hygge.backend.entity.Member;
import hygge.backend.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static hygge.backend.entity.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void MemberRepository가Null이아님() {
        assertThat(memberRepository).isNotNull();
    }

    @Test
    public void 회원등록(){

        //Given
        final Member member = Member.builder()
                .loginId("loginId")
                .email("test@ajou.ac.kr")
                .password("1234")
                .role(ROLE_USER)
                .nickname("test_nickname")
                .build();
        //When
        final Member result = memberRepository.save(member);

        //Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getLoginId()).isEqualTo("loginId");
        assertThat(result.getEmail()).isEqualTo("test@ajou.ac.kr");
        assertThat(result.getPassword()).isEqualTo("1234");
        assertThat(result.getRole()).isEqualTo(ROLE_USER);
        assertThat(result.getNickname()).isEqualTo("test_nickname");
    }

}
