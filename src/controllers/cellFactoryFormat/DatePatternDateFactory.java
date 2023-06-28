package controllers.cellFactoryFormat;

import java.time.LocalDate;

import biblioteca.DatePattern;
import javafx.scene.control.TableCell;

public class DatePatternDateFactory extends TableCell<DatePattern, LocalDate> {

    @Override
    protected void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
        } else {
            setText(item.format(DatePattern.DATE_PATTERN));
        }
    }
}
