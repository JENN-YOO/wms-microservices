import msa.userservice.UserServiceApplication;
import msa.userservice.application.port.in.SignUpUseCase;
import msa.userservice.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = UserServiceApplication.class)
@Transactional
public class MemberRepositoryTest {

    @Autowired
    private SignUpUseCase signUpUseCase;

    @Test
    void testSignUpAndFindMember() {
        // given
        Member member = new Member();
        member.setMemberType("USER");
        member.setLoginId("testuser");
        member.setPassword("password123");
        member.setName("Test User");
        member.setEmail("test@example.com");

        // when
        Member savedMember = signUpUseCase.signUp(member);

        // then
        assertNotNull(savedMember.getMemberId()); // Ensure ID is generated
        assertEquals("testuser", savedMember.getLoginId());
        assertEquals("Test User", savedMember.getName());
        System.out.println("Signed Up Member: " + savedMember.getName() + " (" + savedMember.getLoginId() + ")");
    }
}
