package br.com.minhaudocao.adote.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Embeddable
public class DataId implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddata")
    private Long id;

    private Long idevento;

    public DataId(Long id, Long idevento) {
        this.id = id;
        this.idevento = idevento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdevento() {
        return idevento;
    }

    public void setIdevento(Long idevento) {
        this.idevento = idevento;
    }
}
