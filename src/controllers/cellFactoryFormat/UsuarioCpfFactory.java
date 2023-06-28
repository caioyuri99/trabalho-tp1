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
            String cpf = Usuario.formatCPF(item);
            setText(cpf);
        }
    }

}
