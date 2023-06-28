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

    public abstract void alterarSenha(String senhaAtual, String novaSenha) throws Exception;

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

    public static String formatCPF(String cpf) {
        // Remove caracteres não numéricos do CPF
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se o CPF possui 11 dígitos
        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF inválido");
        }

        // Formata o CPF adicionando os pontos e o traço
        cpf = cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);

        return cpf;
    }

}
