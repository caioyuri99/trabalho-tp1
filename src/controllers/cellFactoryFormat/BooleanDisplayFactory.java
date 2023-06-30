package controllers.cellFactoryFormat;

import biblioteca.BooleanDisplayPattern;
import javafx.scene.control.TableCell;

public class BooleanDisplayFactory extends TableCell<BooleanDisplayPattern, Boolean> {

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
