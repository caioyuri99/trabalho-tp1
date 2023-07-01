package biblioteca;

import dao.RevistaDAO;

public class Revista extends Item {
    // ATRIBUTOS
    private String categoria;
    private int numero;

    // CONSTRUTORES
    public Revista() {
    }

    public Revista(String editora, int edicao, String condicao, String categoria, int numero) {
        super(editora, edicao, condicao);
        this.categoria = categoria;
        this.numero = numero;
    }

    public Revista(String editora, int edicao, String condicao, Obra obra, String categoria, int numero) {
        super(editora, edicao, condicao, obra);
        this.categoria = categoria;
        this.numero = numero;
    }

    public Revista(String editora, int edicao, String condicao, boolean disponivel, Obra obra, String categoria, int numero) {
        super(editora, edicao, condicao, disponivel, obra);
        this.categoria = categoria;
        this.numero = numero;
    }

    // MÉTODOS
    @Override
    public void cadastrar() throws Exception {
        RevistaDAO dao = new RevistaDAO();

        dao.insert(this);
    }

    @Override
    public void atualizar() throws Exception {
        RevistaDAO dao = new RevistaDAO();

        dao.update(this);
    }

    @Override
    public void remover() throws Exception {
        RevistaDAO dao = new RevistaDAO();

        dao.delete(this.id);
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
        return this.categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getNumero() {
        return this.numero;
    }

    public void setNumero(int numero) throws Exception {
        if (numero <= 0) {
            throw new Exception("Número inválido");
        }

        this.numero = numero;
    }
}
