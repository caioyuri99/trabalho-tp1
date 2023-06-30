package controllers.cellFactoryFormat;

import biblioteca.Emprestimo;
import biblioteca.Item;
import javafx.scene.control.TableCell;

public class EmprestimoItemObraAutorFactory extends TableCell<Emprestimo, Item> {

    @Override
    protected void updateItem(Item item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
        } else {
            setText(item.getObra().getAutor());
        }
    }

}
