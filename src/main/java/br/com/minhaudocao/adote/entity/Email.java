package br.com.minhaudocao.adote.entity;

public class Email {
    String to;
    String subject;
    String text;

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }

}
