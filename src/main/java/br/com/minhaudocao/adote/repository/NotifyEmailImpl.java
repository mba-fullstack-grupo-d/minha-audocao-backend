package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Component
public class NotifyEmailImpl implements NotifyRepository {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void send(Email email) {
        MimeMessage msg = javaMailSender.createMimeMessage();
        try {
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getTo()));
            msg.setSubject(email.getSubject());
            msg.setContent(email.getText(), "text/html");
            javaMailSender.send(msg);
        } catch (AddressException e) {
            System.out.println(e);
        } catch (MessagingException e) {
            System.out.println(e);
        }
    }
}
