package hygge.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;

    public static final String code = createCode();

    private MimeMessage createMessage(String to) throws Exception {
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);  // 받는 사람
        message.setSubject("[아주좋은팀]회원가입 인증 이메일");  // 제목

        String msgg = "";
        msgg+="<div><h2>회원 가입 인증 코드 입니다.</h2>";
        msgg+="CODE : <strong>";
        msgg+=code+"</strong></div>";
        message.setText(msgg, "utf-8", "html");  // 내용
        message.setFrom(new InternetAddress("ajou.sw.hygge@gmail.com", "아주좋은팀"));  // 보내는 사람

        return message;
    }

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
        MimeMessage message = createMessage(to);
        try {
            emailSender.send(message);
        } catch (MailException e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return code;
    }
}
