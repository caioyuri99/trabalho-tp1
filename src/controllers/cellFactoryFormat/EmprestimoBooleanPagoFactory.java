package controllers.cellFactoryFormat;

import biblioteca.Emprestimo;
import javafx.scene.control.TableCell;

public class EmprestimoBooleanPagoFactory extends TableCell<Emprestimo, Boolean> {

    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
        } else if (item == null) {
            setText("Sem multa");
        } else {
            if (item) {
                setText("Pago");
            } else {
                setText("Não pago");
            }
        }
    }

}
