package financesapp;
 
import java.time.LocalDate;

abstract class Transaction {
    private float value;
    private LocalDate date;
    private int number;
    private boolean concretized;
    private String description;
    private String information;
}
