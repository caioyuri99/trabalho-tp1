package controllers.cellFactoryFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import controllers.tabledata.ObraData;
import javafx.scene.control.TableCell;

public class ObraDataPublicacaoFactory extends TableCell<ObraData, LocalDate> {

    @Override
    protected void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
        } else {
            setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
    }

}
