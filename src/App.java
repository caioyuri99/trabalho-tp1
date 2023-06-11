import java.util.ArrayList;

import biblioteca.Cliente;
import biblioteca.Item;
import biblioteca.Livro;
import dao.ClienteDAO;
import dao.LivroDAO;

public class App {
    public static void main(String[] args) throws Exception {
        Cliente c = new ClienteDAO().getCliente("11111111111");

        LivroDAO dao = new LivroDAO();
        
        ArrayList<Item> res = dao.getLoans(c);

        for (Item item : res) {
            System.out.println((Livro) item);
        }
    }
}
