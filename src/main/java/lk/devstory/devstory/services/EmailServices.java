package lk.devstory.devstory.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServices {
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String body, String topic) {
        log.info("Initializing Email Sender Services");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("dev.devstory@gmail.com");
        mailMessage.setTo(to);
        mailMessage.setSubject(topic);
        mailMessage.setText(body);

        log.info("Sending Email to "+to);
        javaMailSender.send(mailMessage);
    }
}
