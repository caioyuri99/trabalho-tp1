package biblioteca;

import java.time.format.DateTimeFormatter;

public interface DatePattern {
    public static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd/MM/yyyy");
}
