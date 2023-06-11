import java.util.ArrayList;

import biblioteca.Cliente;
import biblioteca.Emprestimo;
import biblioteca.Item;
import dao.ClienteDAO;
import dao.GibiDAO;
import dao.LivroDAO;

public class App {
    public static void main(String[] args) throws Exception {
        ArrayList<Item> itens = new ArrayList<Item>();

        itens.add(new GibiDAO().getGibi(1));
        itens.add(new LivroDAO().getLivro(1));

        Cliente c = new ClienteDAO().getCliente("11111111111");
        Emprestimo e = new Emprestimo(itens, c);

        c.fazerEmprestimo(e);
    }
}
