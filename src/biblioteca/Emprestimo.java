package biblioteca;

import java.util.ArrayList;
import java.time.LocalDate;

public class Emprestimo {
    // ATRIBUTOS
    // static final double valor_multa = 3.0; <- Ideia de multa fixa.
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private ArrayList<Item> itens = new ArrayList<>();
    private Cliente cliente;

    // CONSTRUTORES
    public Emprestimo() {
    }

    public Emprestimo(ArrayList<Item> itens, Cliente cliente) {
        this.itens = itens;
        this.cliente = cliente;
    }

    public Emprestimo(LocalDate dataDevolucao, LocalDate dataEmprestimo, ArrayList<Item> itens, Cliente cliente) {
        this.dataDevolucao = dataDevolucao;
        this.dataEmprestimo = dataEmprestimo;
        this.itens = itens;
        this.cliente = cliente;
    }

    // GETTERS & SETTERS
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

    public ArrayList<Item> getItems() {
        return this.itens;
    }

    public void setItens(ArrayList<Item> itens) {
        this.itens = itens;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

}
