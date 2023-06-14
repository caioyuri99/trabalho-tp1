package biblioteca;

import java.time.LocalDate;

import dao.LivroDAO;

public class Livro extends Item {
    // ATRIBUTOS
    private String tipoCapa;
    private LocalDate dataLancamento;

    // CONSTRUTORES
    public Livro() {
    }

    public Livro(String editora, int edicao, String condicao, Obra obra, String tipoCapa, LocalDate dataLancamento) {
        super(editora, edicao, condicao, obra);
        this.tipoCapa = tipoCapa;
        this.dataLancamento = dataLancamento;
    }

    public Livro(
            String editora,
            int edicao,
            String condicao,
            boolean disponivel,
            Obra obra,
            String tipoCapa,
            LocalDate dataLancamento) {
        super(editora, edicao, condicao, disponivel, obra);
        this.tipoCapa = tipoCapa;
        this.dataLancamento = dataLancamento;
    }

    // MÉTODOS
    @Override
    public boolean cadastrar() {
        LivroDAO dao = new LivroDAO();

        return dao.insert(this);
    }

    @Override
    public boolean remover() {
        LivroDAO dao = new LivroDAO();

        return dao.delete(this.id);
    }

    @Override
    public boolean emprestar(Cliente leitor) {
        this.leitor = leitor;
        this.disponivel = false;

        LivroDAO dao = new LivroDAO();

        return dao.updateLeitor(this, leitor, false);
    }

    // GETTERS & SETTERS
    public String getTipoCapa() {
        return tipoCapa;
    }

    public void setTipoCapa(String tipoCapa) {
        this.tipoCapa = tipoCapa;
    }

    public LocalDate getDataLancamento() {
        return this.dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

}
