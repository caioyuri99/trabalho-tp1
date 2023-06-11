package biblioteca;

import dao.GibiDAO;

public class Gibi extends Item {
    // ATRIBUTOS
    private String tipo;
    private String categoria;

    // CONSTRUTORES
    public Gibi() {
    };

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
    public boolean cadastrar() {
        GibiDAO dao = new GibiDAO();

        return dao.insert(this);
    }

    @Override
    public boolean remover() {
        GibiDAO dao = new GibiDAO();

        return dao.delete(this.id);
    }

    @Override
    public boolean emprestar(Cliente leitor) {
        this.leitor = leitor;
        this.disponivel = false;

        GibiDAO dao = new GibiDAO();

        return dao.updateLeitor(this, leitor, false);
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
