package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.Email;
import br.com.minhaudocao.adote.repository.NotifyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifierService {
    @Autowired
    private NotifyRepository notifyRepository;

    public void send() {

        Email email = new Email();
        email.setTo("asampaio3006@gmail.com");
        email.setSubject("Formulários Adoção");
        email.setText("Ola, \n Seu cadastro foi realizado com sucesso.\n");

        notifyRepository.send(email);
    }

}
