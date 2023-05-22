package hygge.backend.service;

import hygge.backend.dto.response.EmailAuthResponse;
import hygge.backend.entity.School;
import hygge.backend.error.exception.BusinessException;
import hygge.backend.repository.MemberRepository;
import hygge.backend.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.MimeMessage;
import java.util.Random;

import static hygge.backend.error.exception.ExceptionInfo.*;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final SchoolRepository schoolRepository;
    private final MemberRepository memberRepository;

    public static String createCode() {
        StringBuffer key = new StringBuffer();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {  // 인증코드 8자리
            int index = random.nextInt(3);  // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (random.nextInt(26)) + 97));
                    //  a~z
                    break;
                case 1:
                    key.append((char) ((int) (random.nextInt(26)) + 65));
                    // A~Z
                    break;
                case 2:
                    key.append((random.nextInt(10)));
                    // 0~9
                    break;
            }
        }

        return key.toString();
    }

    @Transactional(readOnly = true)
    public EmailAuthResponse sendEmail(String email) throws Exception {
        int index = email.lastIndexOf("@") + 1;
        String emailForm = email.substring(index);

        if (index <= 1) { // 0일 경우 @ 없음, 1일 경우 이메일 입력 안함.
            throw new BusinessException(INVALID_EMAIL_FORM);
        }

        School school = schoolRepository.findByEmailForm(emailForm)
                .orElseThrow(()-> new BusinessException(UNREGISTERED_EMAIL_FORM));

        if(memberRepository.existsByEmail(email)) throw new BusinessException(REGISTERED_EMAIL);

        final String code = createCode();

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        String msgg = "";
        msgg+="<div><h2>회원 가입 인증 코드 입니다.</h2>";
        msgg+="CODE : <strong>";
        msgg+=code+"</strong></div>";

        helper.setTo(email);
        helper.setFrom("ajou.sw.hygge@gmail.com");
        helper.setSubject("[아주좋은팀] 회원가입 인증 이메일");
        helper.setText(msgg, true);

        try {
            emailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return EmailAuthResponse.builder()
                .schoolId(school.getId())
                .schoolName(school.getSchoolName())
                .code(code)
                .build();
    }
}
