package br.com.minhaudocao.adote.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Data {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddata")
    private Long id;

    @Column(nullable = false)
    private LocalDate data;

    @Column(name = "hora_inicio", nullable = false)
    private Integer horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private Integer horaFim;

    @ManyToOne
    @JoinColumn(name = "idevento")
    private Evento evento;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Integer getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Integer horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Integer getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(Integer horaFim) {
        this.horaFim = horaFim;
    }
}
