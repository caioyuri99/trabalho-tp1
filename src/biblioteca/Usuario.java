package biblioteca;

import java.time.LocalDate;

public abstract class Usuario implements DatePattern {
    // ATRIBUTOS
    protected String senha, nome, cpf;
    protected LocalDate dataNasc;

    // CONSTRUTORES
    public Usuario() {
    }

    public Usuario(String cpf, String senha, String nome, LocalDate dataNasc) {
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
        this.dataNasc = dataNasc;
    }

    // GETTERS & SETTERS
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNasc() {
        return dataNasc;
    }

    public void setDataNasc(LocalDate dataNasc) {
        this.dataNasc = dataNasc;
    }

}
