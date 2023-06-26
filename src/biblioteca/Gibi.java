package biblioteca;

import dao.GibiDAO;

public class Gibi extends Item {
    // ATRIBUTOS
    private String tipo;
    private String categoria;

    // CONSTRUTORES
    public Gibi() {
    };

    public Gibi(String editora, int edicao, String condicao, String tipo, String categoria) {
        super(editora, edicao, condicao);
        this.tipo = tipo;
        this.categoria = categoria;
    }

    public Gibi(String editora, int edicao, String condicao, Obra obra, String tipo, String categoria) {
        super(editora, edicao, condicao, obra);
        this.tipo = tipo;
        this.categoria = categoria;
    }

    public Gibi(
            String editora,
            int edicao,
            String condicao,
            boolean disponivel,
            Obra obra,
            String tipo,
            String categoria) {
        super(editora, edicao, condicao, disponivel, obra);
        this.tipo = tipo;
        this.categoria = categoria;
    }

    // MÉTODOS
    @Override
    public void cadastrar() throws Exception {
        GibiDAO dao = new GibiDAO();

        dao.insert(this);
    }

    @Override
    public boolean remover() {
        GibiDAO dao = new GibiDAO();

        return dao.delete(this.id);
    }

    @Override
    public void emprestar() throws Exception {
        this.disponivel = false;

        GibiDAO dao = new GibiDAO();

        dao.updateLeitor(this, false);
    }

    @Override
    public void devolver() throws Exception {
        this.disponivel = true;

        GibiDAO dao = new GibiDAO();

        dao.updateLeitor(this, true);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Gibi) {
            Gibi gibi = (Gibi) obj;

            return this.id == gibi.id;
        }

        return false;
    }

    // GETTERS & SETTTERS
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

}
