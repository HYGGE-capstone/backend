package hygge.backend.service;

import hygge.backend.dto.response.CodeResponse;
import hygge.backend.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final SchoolRepository schoolRepository;

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

    public CodeResponse sendEmail(String to) throws Exception {

        if (!emailIsValidate(to)) {
            throw new RuntimeException("등록되지 않은 이메일 형식입니다.");
        }

        final String code = createCode();

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        String msgg = "";
        msgg+="<div><h2>회원 가입 인증 코드 입니다.</h2>";
        msgg+="CODE : <strong>";
        msgg+=code+"</strong></div>";

        helper.setTo(to);
        helper.setFrom("ajou.sw.hygge@gmail.com");
        helper.setSubject("[아주좋은팀]회원가입 인증 이메일");
        helper.setText(msgg, true);

        try {
            emailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return new CodeResponse(code);
    }

    @Transactional(readOnly = true)
    public boolean emailIsValidate(String email) {
        // 이메일을 @ 기준으로 나눠서 이메일 폼만 저장한다.
        int index = email.lastIndexOf("@") + 1;
        String emailForm = email.substring(index);
        // 학교에서 해당하는 이메일 폼이 있는지 확인한다.
        return schoolRepository.existsByEmailForm(emailForm);
    }

}
