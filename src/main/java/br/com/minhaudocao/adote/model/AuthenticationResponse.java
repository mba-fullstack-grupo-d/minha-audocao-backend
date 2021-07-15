package br.com.minhaudocao.adote.model;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


public class AuthenticationResponse implements Serializable {

    private final String jwt;

    private List<String> roles;

    private Long id;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return jwt;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRole(Collection roles) {
        this.roles = (List<String>) roles.stream().map((auth) -> ((SimpleGrantedAuthority)auth).getAuthority()).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

