package controllers.cellFactoryFormat;

import biblioteca.Emprestimo;
import javafx.scene.control.TableCell;

public class EmprestimoDoubleValorMultaFactory extends TableCell<Emprestimo, Double> {

    @Override
    protected void updateItem(Double item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText("Sem multa");
        } else {
            setText(String.format("R$ %.2f", item));
        }
    }

}
