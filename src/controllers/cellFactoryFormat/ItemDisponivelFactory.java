package controllers.cellFactoryFormat;

import biblioteca.Item;
import javafx.scene.control.TableCell;

public class ItemDisponivelFactory extends TableCell<Item, Boolean> {

    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
        } else {
            if (item) {
                setText("Disponível");
            } else {
                setText("Indisponível");
            }
        }
    }
}
