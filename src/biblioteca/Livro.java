package biblioteca;

import dao.LivroDAO;

public class Livro extends Item {
    // ATRIBUTOS
    private String tipoCapa;

    // CONSTRUTORES
    public Livro() {
    }

    public Livro(String editora, int edicao, String condicao, Obra obra, String tipoCapa) {
        super(editora, edicao, condicao, obra);
        this.tipoCapa = tipoCapa;
    }

    public Livro(
            String editora,
            int edicao,
            String condicao,
            boolean disponivel,
            Obra obra,
            String tipoCapa) {
        super(editora, edicao, condicao, disponivel, obra);
        this.tipoCapa = tipoCapa;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Livro) {
            Livro livro = (Livro) obj;

            return this.id == livro.id;
        }

        return false;
    }

    // GETTERS & SETTERS
    public String getTipoCapa() {
        return tipoCapa;
    }

    public void setTipoCapa(String tipoCapa) {
        this.tipoCapa = tipoCapa;
    }

}
