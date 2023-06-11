import biblioteca.Cliente;
import biblioteca.Item;
import biblioteca.Obra;
import dao.ClienteDAO;
import dao.EmprestimoDAO;
import dao.ObraDAO;

public class App {
    public static void main(String[] args) throws Exception {
        Obra o = new ObraDAO().getObra(4);

        Item i = o.getItens().get(0);

        Cliente c = new ClienteDAO().getCliente("11111111111");

        EmprestimoDAO dao = new EmprestimoDAO();

        dao.insert(c, i);
    }
}
