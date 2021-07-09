package br.com.minhaudocao.adote.exception;

public class EmailExistsException extends Exception{
    public EmailExistsException(String message) {
        super(message);
    }
}
