package biblioteca;

import java.util.ArrayList;

import dao.EstanteDAO;
import dao.ObraDAO;

public class Estante {
    // ATRIBUTOS
    private int id;
    private String categoria;
    private ArrayList<Obra> obras = new ArrayList<>();

    // CONSTUTORES
    public Estante() {
    }

    public Estante(String categoria) {
        this.categoria = categoria;
    }

    public Estante(int id, String categoria) {
        this.id = id;
        this.categoria = categoria;
    }

    public Estante(int id, String categoria, ArrayList<Obra> obras) {
        this.id = id;
        this.categoria = categoria;
        this.obras = obras;
    }

    // MÉTODOS
    public void adicionarEstante() throws Exception {
        EstanteDAO dao = new EstanteDAO();

        try {
            dao.insert(this);

        } catch (Exception e) {
            throw new Exception("Erro ao cadastrar estante: " + e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public void editarEstante() throws Exception {
        EstanteDAO dao = new EstanteDAO();

        if (dao.estanteExists(this.categoria)) {
            throw new Exception("Já existe uma estante com essa categoria!");
        }

        try {
            dao.update(this);

        } catch (Exception e) {
            throw new Exception("Erro ao atualizar estante: " + e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public void deletarEstante() throws Exception {
        EstanteDAO dao = new EstanteDAO();

        try {
            dao.delete(this.id);

        } catch (Exception e) {
            throw new Exception("Erro ao deletar estante: " + e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public void registrarObra(Obra obra) throws Exception {
        ObraDAO dao = new ObraDAO();

        try {
            dao.insert(obra, this);

        } catch (Exception e) {
            throw new Exception("Erro ao registrar obra: " + e.getMessage());

        } finally {
            dao.closeConnection();
        }

        obra.setEstante(this);
        obras.add(obra);
    }

    public void removerObra(Obra obra) throws Exception {
        ObraDAO dao = new ObraDAO();

        obra.removeAllItems();

        try {
            dao.delete(obra.getId());

        } catch (Exception e) {
            throw new Exception("Erro ao remover obra: " + e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public void atualizarObra(Obra obra) throws Exception {
        ObraDAO dao = new ObraDAO();

        try {
            dao.updateObra(this, obra);

        } catch (Exception e) {
            throw new Exception("Erro ao atualizar obra: " + e.getMessage());

        } finally {
            dao.closeConnection();
        }
    }

    public static ArrayList<Estante> getListaEstantes() {
        EstanteDAO dao = new EstanteDAO();

        ArrayList<Estante> estantes = dao.getAll();

        dao.closeConnection();

        return estantes;
    }

    public static boolean estanteExiste(String categoria) {
        EstanteDAO dao = new EstanteDAO();

        boolean existe = dao.estanteExists(categoria);

        dao.closeConnection();

        return existe;
    }

    // GETTERS & SETTERS
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public ArrayList<Obra> getListaObras() {
        return this.obras;
    }

    public void setListaObras(ArrayList<Obra> obras) {
        this.obras = obras;
    }

    @Override
    public String toString() {
        return this.categoria;
    }

}
