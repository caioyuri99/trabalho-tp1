package controllers.cellFactoryFormat;

import biblioteca.Usuario;
import javafx.scene.control.TableCell;

public class UsuarioCpfFactory extends TableCell<Usuario, String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
        } else {
            String cpf = item.substring(0, 3) + "." + item.substring(3, 6) + "." + item.substring(6, 9) + "-"
                    + item.substring(9, 11);
            setText(cpf);
        }
    }

}
