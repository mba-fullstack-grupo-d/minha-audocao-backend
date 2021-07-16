package br.com.minhaudocao.adote.repository;

import br.com.minhaudocao.adote.entity.Email;

public interface NotifyRepository {
    public void send(Email email);
}
