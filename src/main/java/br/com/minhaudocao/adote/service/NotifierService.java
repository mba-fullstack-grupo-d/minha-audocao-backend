package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.Email;
import br.com.minhaudocao.adote.entity.Instituicao;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.repository.EnderecoRepository;
import br.com.minhaudocao.adote.repository.NotifyRepository;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.midi.Soundbank;

@Service
public class NotifierService {

    @Autowired
    private InstituicaoService instituicaoService;

    @Autowired
    private NotifyRepository notifyRepository;

    public void send(Long id, String text) throws ResourceNotFoundException {

        Instituicao instituicao = instituicaoService.getById(id);

        Email email = new Email();
        email.setTo(instituicao.getEmail());
        email.setSubject("Interesse Adoção");
        email.setText(getStringEscapeU(text));

        notifyRepository.send(email);
    }

    private String getStringEscapeU(String text) {
        String replaceOne = text.replace("{", "");
        String replaceTwo = replaceOne.replace("}", "");
        String replaceThree = replaceTwo.replace("\"form\":", "");
        String textMsg = replaceThree.replace("\"", "");
        String outText = StringEscapeUtils.unescapeJava(textMsg);
        return outText;
    }

}
