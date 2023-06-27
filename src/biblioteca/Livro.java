package biblioteca;

import dao.LivroDAO;

public class Livro extends Item {
    // ATRIBUTOS
    private String tipoCapa;

    // CONSTRUTORES
    public Livro() {
    }

    public Livro(String editora, int edicao, String condicao, String tipoCapa) {
        super(editora, edicao, condicao);
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
    public void cadastrar() throws Exception {
        LivroDAO dao = new LivroDAO();

        dao.insert(this);
    }

    @Override
    public void atualizar() throws Exception {
        LivroDAO dao = new LivroDAO();

        dao.update(this);
    }

    @Override
    public void remover() throws Exception {
        LivroDAO dao = new LivroDAO();

        dao.delete(this.id);
    }

    @Override
    public void emprestar() throws Exception {
        this.disponivel = false;

        LivroDAO dao = new LivroDAO();

        dao.updateLeitor(this, false);
    }

    @Override
    public void devolver() throws Exception {
        this.disponivel = true;

        LivroDAO dao = new LivroDAO();

        dao.updateLeitor(this, true);
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
