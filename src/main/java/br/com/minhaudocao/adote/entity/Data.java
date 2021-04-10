package br.com.minhaudocao.adote.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Data {

    @EmbeddedId
    private DataId dataId;

    private LocalDate data;

    @Column(name = "hora_inicio")
    private Integer horaInicio;

    @Column(name = "hora_fim")
    private Integer horaFim;

    public DataId getDataId() {
        return dataId;
    }

    public void setDataId(DataId dataId) {
        this.dataId = dataId;
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
