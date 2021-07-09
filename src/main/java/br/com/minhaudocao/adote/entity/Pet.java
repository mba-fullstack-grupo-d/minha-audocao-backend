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

    private String raca;

    private Integer idade;

    @ManyToOne
    @JoinColumn(name = "idinstituicao")
    private Instituicao instituicao;

    @Transient
    private ArrayList<MultipartFile> fotos;

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
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

    public ArrayList<MultipartFile> getFotos() {
        return fotos;
    }

    public void setFotos(ArrayList<MultipartFile> fotos) {
        this.fotos = fotos;
    }

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", especie='" + especie + '\'' +
                ", descricao='" + descricao + '\'' +
                ", adotado=" + adotado +
                ", raca='" + raca + '\'' +
                ", idade=" + idade +
                ", instituicao=" + instituicao +
                '}';
    }
}
