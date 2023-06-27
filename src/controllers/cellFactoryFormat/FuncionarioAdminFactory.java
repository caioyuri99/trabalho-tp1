package controllers.cellFactoryFormat;

import biblioteca.Funcionario;
import javafx.scene.control.TableCell;

public class FuncionarioAdminFactory extends TableCell<Funcionario, Boolean> {

    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
        } else {
            if (item) {
                setText("Sim");
            } else {
                setText("Não");
            }
        }
    }

}
