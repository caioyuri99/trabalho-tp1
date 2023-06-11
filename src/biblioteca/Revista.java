package biblioteca;

import dao.RevistaDAO;

public class Revista extends Item {
    // ATRIBUTOS
    private String categoria;

    // CONSTRUTORES
    public Revista() {
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
    public boolean cadastrar() {
        RevistaDAO dao = new RevistaDAO();

        return dao.insert(this);
    }

    @Override
    public boolean remover() {
        RevistaDAO dao = new RevistaDAO();

        return dao.delete(this.id);
    }

    // GETTERS & SETTERS
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
