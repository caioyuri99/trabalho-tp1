package biblioteca;

import dao.GibiDAO;

public class Gibi extends Item {
    // ATRIBUTOS
    private String tipo;

    // CONSTRUTORES
    public Gibi() {
    };

    public Gibi(String editora, int edicao, String condicao, String tipo) {
        super(editora, edicao, condicao);
        this.tipo = tipo;
    }

    public Gibi(String editora, int edicao, String condicao, Obra obra, String tipo) {
        super(editora, edicao, condicao, obra);
        this.tipo = tipo;
    }

    public Gibi(
            String editora,
            int edicao,
            String condicao,
            boolean disponivel,
            Obra obra,
            String tipo) {
        super(editora, edicao, condicao, disponivel, obra);
        this.tipo = tipo;
    }

    // MÉTODOS
    @Override
    public void cadastrar() throws Exception {
        GibiDAO dao = new GibiDAO();

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
        GibiDAO dao = new GibiDAO();

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
        GibiDAO dao = new GibiDAO();

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

        GibiDAO dao = new GibiDAO();

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

        GibiDAO dao = new GibiDAO();

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

}
