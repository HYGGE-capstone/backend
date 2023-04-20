package hygge.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;

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

    public String sendEmail(String to) throws Exception {
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
        return code;
    }
}
