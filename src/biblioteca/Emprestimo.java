package biblioteca;

import java.util.ArrayList;

import dao.EmprestimoDAO;

import java.time.LocalDate;

public class Emprestimo {
    // ATRIBUTOS
    // static final double valor_multa = 3.0; <- Ideia de multa fixa.
    private int id;
    private String tipoItem;
    private Item item;
    private Cliente leitor;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private int qtdRenovacoes;
    private boolean devolvido;
    private boolean atrasado;
    private boolean multado;
    private double valorMulta;

    // CONSTRUTORES
    public Emprestimo() {
    }

    public Emprestimo(Cliente leitor) {
        this.leitor = leitor;
    }

    public Emprestimo(LocalDate dataDevolucao, LocalDate dataEmprestimo, Cliente leitor) {
        this.dataDevolucao = dataDevolucao;
        this.dataEmprestimo = dataEmprestimo;
        this.leitor = leitor;
    }

    // MÉTODOS
    public Item getItemEmprestado() {
        EmprestimoDAO dao = new EmprestimoDAO();

        return dao.getItemFromEmprestimo(this);
    }

    // GETTERS & SETTERS
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoItem() {
        return this.tipoItem;
    }

    public void setTipoItem(String tipoItem) {
        this.tipoItem = tipoItem;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Cliente getLeitor() {
        return this.leitor;
    }

    public void setLeitor(Cliente leitor) {
        this.leitor = leitor;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public int getQtdRenovacoes() {
        return this.qtdRenovacoes;
    }

    public void setQtdRenovacoes(int qtdRenovacoes) {
        this.qtdRenovacoes = qtdRenovacoes;
    }

    public boolean isDevolvido() {
        return this.devolvido;
    }

    public void setDevolvido(boolean devolvido) {
        this.devolvido = devolvido;
    }

    public boolean isAtrasado() {
        return this.atrasado;
    }

    public void setAtrasado(boolean atrasado) {
        this.atrasado = atrasado;
    }

    public boolean isMultado() {
        return this.multado;
    }

    public void setMultado(boolean multado) {
        this.multado = multado;
    }

    public double getValorMulta() {
        return this.valorMulta;
    }

    public void setValorMulta(double valorMulta) {
        this.valorMulta = valorMulta;
    }

}
