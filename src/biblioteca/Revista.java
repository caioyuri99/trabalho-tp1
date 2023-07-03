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

        try {
            dao.insert(this);

        } catch (Exception e) {
            throw new Exception("Erro ao cadastrar: " + e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    @Override
    public void atualizar() throws Exception {
        RevistaDAO dao = new RevistaDAO();

        try {
            dao.update(this);

        } catch (Exception e) {
            throw new Exception("Erro ao atualizar: " + e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    @Override
    public void remover() throws Exception {
        RevistaDAO dao = new RevistaDAO();

        try {
            dao.delete(this.id);

        } catch (Exception e) {
            throw new Exception("Erro ao remover: " + e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    @Override
    public void emprestar() throws Exception {
        this.disponivel = false;

        RevistaDAO dao = new RevistaDAO();

        try {
            dao.updateLeitor(this, false);

        } catch (Exception e) {
            throw new Exception("Erro ao emprestar: " + e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    @Override
    public void devolver() throws Exception {
        this.disponivel = true;

        RevistaDAO dao = new RevistaDAO();

        try {
            dao.updateLeitor(this, true);

        } catch (Exception e) {
            throw new Exception("Erro ao devolver: " + e.getMessage());

        } finally {
            dao.closeConnection();
        }
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
