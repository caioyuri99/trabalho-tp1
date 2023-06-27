package controllers.cellFactoryFormat;

import biblioteca.Cliente;
import javafx.scene.control.TableCell;

public class ClienteSaldoDevedorFactory extends TableCell<Cliente, Double> {

    @Override
    protected void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
        } else {
            setText(String.format("R$ %.2f", item));
        }
    }

}
