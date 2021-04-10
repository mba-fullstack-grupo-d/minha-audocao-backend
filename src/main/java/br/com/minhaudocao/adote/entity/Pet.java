package br.com.minhaudocao.adote.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpet")
    private Long id;

    private String nome;

    private String especie;

    private String descricao;

    private String imagem;

    private Boolean adotado;

    private String raca;

    private Integer idade;

    @ManyToOne
    @JoinColumn(name = "idinstituicao")
    private Instituicao instituicao;

    public Integer getIdade(){
        return idade;
    }

    public void setIdade(Integer idade){
        this.idade = idade;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public Boolean getAdotado(){
        return adotado;
    }

    public void setAdotado(Boolean adotado){
        this.adotado = adotado;
    }

    public Instituicao getInstituicao(){
        return instituicao;
    }

    public void setInstituicao(Instituicao instituicao){
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

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
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

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", especie='" + especie + '\'' +
                ", descricao='" + descricao + '\'' +
                ", imagem='" + imagem + '\'' +
                ", adotado=" + adotado +
                ", raca='" + raca + '\'' +
                ", idade=" + idade +
                ", instituicao=" + instituicao +
                '}';
    }
}
