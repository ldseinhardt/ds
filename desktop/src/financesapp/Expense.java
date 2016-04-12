package financesapp;
 
import java.time.LocalDate;

public class Expense extends Transaction {
    
    //Categoria
    private ExpenseCategory category;
    
    public Expense(double value, LocalDate date, int number, boolean concretized, String description, String information) {
        super(value, date, number, concretized, description, information);
    }
    
    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }
    
    public ExpenseCategory getCategory() {
        return this.category;
    }
          
    public double getValue() {
        return this.value * - 1;
    }  
}
