package financesapp;
 
import java.time.LocalDate;

public class Expense extends Transaction {
    
    //Categoria
    private ExpenseCategory category;
    
    public Expense() {
        super(0, LocalDate.now(), 1, true, "", "");
        this.category = null;
    }
    
    public Expense(double value, LocalDate date, int number, boolean concretized, String description, String information) {
        super(value, date, number, concretized, description, information);
        this.category = null;
    }
    
    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }
    
    public ExpenseCategory getCategory() {
        return this.category;
    }
          
    public double getRealValue() {
        return this.value  * this.number * - 1;
    }  
}
