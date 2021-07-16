package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class NotifyEmailImpl implements NotifyRepository {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void send(Email email) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email.getTo());
        msg.setSubject(email.getSubject());
        msg.setText(email.getText());

        javaMailSender.send(msg);
    }
}
