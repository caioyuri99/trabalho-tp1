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
    
    public Livro(String editora, int edicao, String condicao, String tipoCapa, Obra obra) {
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
    public void cadastrar() throws Exception {
        LivroDAO dao = new LivroDAO();

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
        LivroDAO dao = new LivroDAO();


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
        LivroDAO dao = new LivroDAO();

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

        LivroDAO dao = new LivroDAO();

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

        LivroDAO dao = new LivroDAO();

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
