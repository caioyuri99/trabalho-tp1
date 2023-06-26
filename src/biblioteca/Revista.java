package biblioteca;

import dao.RevistaDAO;

public class Revista extends Item {
    // ATRIBUTOS
    private String categoria;

    // CONSTRUTORES
    public Revista() {
    }

    public Revista(String editora, int edicao, String condicao, String categoria) {
        super(editora, edicao, condicao);
        this.categoria = categoria;
    }

    public Revista(String editora, int edicao, String condicao, Obra obra, String categoria) {
        super(editora, edicao, condicao, obra);
        this.categoria = categoria;
    }

    public Revista(String editora, int edicao, String condicao, boolean disponivel, Obra obra, String categoria) {
        super(editora, edicao, condicao, disponivel, obra);
        this.categoria = categoria;
    }

    // MÉTODOS
    @Override
    public void cadastrar() throws Exception {
        RevistaDAO dao = new RevistaDAO();

        dao.insert(this);
    }

    @Override
    public boolean remover() {
        RevistaDAO dao = new RevistaDAO();

        return dao.delete(this.id);
    }

    @Override
    public void emprestar() throws Exception {
        this.disponivel = false;

        RevistaDAO dao = new RevistaDAO();

        dao.updateLeitor(this, false);
    }

    @Override
    public void devolver() throws Exception {
        this.disponivel = true;

        RevistaDAO dao = new RevistaDAO();

        dao.updateLeitor(this, true);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Revista) {
            Revista revista = (Revista) obj;

            return this.id == revista.id;
        }

        return false;
    }

    // GETTERS & SETTERS
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
