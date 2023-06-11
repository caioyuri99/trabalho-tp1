import biblioteca.Cliente;
import biblioteca.Item;
import biblioteca.Livro;
import biblioteca.Obra;
import dao.ClienteDAO;
import dao.LivroDAO;
import dao.ObraDAO;

public class App {
    public static void main(String[] args) throws Exception {
        Obra o = new ObraDAO().getObra(4);

        Item i = o.getItens().get(0);

        Cliente c = new ClienteDAO().getCliente("11111111111");

        LivroDAO dao = new LivroDAO();

        dao.updateLeitor((Livro) i, c, false);
    }
}
