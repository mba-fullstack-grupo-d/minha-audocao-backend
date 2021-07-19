package br.com.minhaudocao.adote.service;

import br.com.minhaudocao.adote.entity.*;
import br.com.minhaudocao.adote.exception.ResourceNotFoundException;
import br.com.minhaudocao.adote.repository.NotifyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifierService {

    @Autowired
    private InstituicaoService instituicaoService;

    @Autowired
    private NotifyRepository notifyRepository;

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private PetService petService;

    public void send(FormularioSend formulario) throws ResourceNotFoundException {

        Instituicao instituicao = instituicaoService.getById(formulario.getIdInstituicao());
        Pessoa pessoa = pessoaService.getById(formulario.getIdPessoa());
        Pet pet = petService.getById(formulario.getIdPet());
        String mensagem = getTemplateMensagem(instituicao,pessoa,pet, formulario);

        Email email = new Email();
        email.setTo(instituicao.getEmail());
        email.setSubject("Interesse Adoção");
        email.setText(mensagem);

        notifyRepository.send(email);
    }

    private String getTemplateMensagem(Instituicao instituicao, Pessoa pessoa, Pet pet , FormularioSend formulario){
        return "<html>\n" +
                "<head>" +
                "<meta http-equiv=”Content-Type” content=”text/html; charset=utf-8″>"+
                "<title>Minha Adocao</title>"+
                "</head>\n" +
                "<body>\n" +
                "    <p><b>Formulario de Adocao</b></p>\n" +
                "    <p><b>Nome Instituicao: </b>" + instituicao.getNome() + "\n"+
                "    <p>Nome Pet: " + pet.getNome() + "\n"+
                "    <p>Especie do Pet: " + pet.getEspecie() + "\n"+
                "    <p>Idade do Pet: " + pet.getIdade() + "\n"+
                "    <p><b>Dados do Adotante: </b>\n"+
                "    <p>Nome: " + pessoa.getNome() + "\n"+
                "    <p>Telefone: " +pessoa.getTelefone() + "\n"+
                "    <p>Email: " + pessoa.getEmail() + "\n"+
                "    <p>Mensagem: " + formulario.getMensagem() + "\n"+
                "    <p>Obrigado</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }
}
