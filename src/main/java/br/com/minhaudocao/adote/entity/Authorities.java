package br.com.minhaudocao.adote.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Authorities {

    @EmbeddedId
    private AuthoritiesPK id;

    public AuthoritiesPK getId() {
        return id;
    }

    public void setId(AuthoritiesPK id) {
        this.id = id;
    }
}
