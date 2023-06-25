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

    public Estante(int id, String categoria, ArrayList<Obra> obras) {
        this.id = id;
        this.categoria = categoria;
        this.obras = obras;
    }

    // MÉTODOS
    public void adicionarEstante() throws Exception {
        EstanteDAO dao = new EstanteDAO();
        dao.insert(this);
    }

    public boolean registrarObra(Obra obra) {
        ObraDAO dao = new ObraDAO();
        boolean res = dao.insert(obra, this);

        if (!res) {
            return false;
        }

        obra.setEstante(this);
        obras.add(obra);

        return true;
    }

    public boolean removerObra(Obra obra) {
        ObraDAO dao = new ObraDAO();

        return dao.delete(obra.getId());
    }

    public static ArrayList<Estante> getListaEstantes() {
        EstanteDAO dao = new EstanteDAO();

        return dao.getAll();
    }

    public static boolean estanteExiste(String categoria) {
        EstanteDAO dao = new EstanteDAO();

        return dao.estanteExists(categoria);
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
