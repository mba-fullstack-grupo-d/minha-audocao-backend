package br.com.minhaudocao.adote.entity;


import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpet")
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String especie;
    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private Boolean adotado;

    private String genero;

    private Integer idade;

    @ManyToOne
    @JoinColumn(name = "idinstituicao")
    private Instituicao instituicao;

    private Boolean vacinado;

    private Boolean castrado;



    @Transient
    private List<String> uriFotos;

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Boolean getAdotado() {
        return adotado;
    }

    public void setAdotado(Boolean adotado) {
        this.adotado = adotado;
    }

    public Instituicao getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(Instituicao instituicao) {
        this.instituicao = instituicao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<String> getUriFotos() {
        return uriFotos;
    }

    public void setUriFotos(List<String> uriFotos) {
        this.uriFotos = uriFotos;
    }

    public void setUriFoto(String uriFoto) {
        this.getUriFotos().add(uriFoto);
    }

    public Boolean getVacinado() {
        return vacinado;
    }

    public void setVacinado(Boolean vacinado) {
        this.vacinado = vacinado;
    }

    public Boolean getCastrado() {
        return castrado;
    }

    public void setCastrado(Boolean castrado) {
        this.castrado = castrado;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", especie='" + especie + '\'' +
                ", descricao='" + descricao + '\'' +
                ", adotado=" + adotado +
                ", genero='" + genero + '\'' +
                ", idade=" + idade +
                ", instituicao=" + instituicao +
                '}';
    }
}
