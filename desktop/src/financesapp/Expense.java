package financesapp;
 
import java.time.LocalDate;
import java.util.Iterator;

public class Expense extends Transaction {
    
    //Categoria
    private ExpenseCategory category;
    
    public Expense(Account account) {
        super(account, LocalDate.now(), "", "");
        this.category = null;
    }
    
    public Expense(Account account, LocalDate date, String description, String information) {
        super(account, date, description, information);
        this.category = null;
    }
    
    public void setCategory(Category category) {
        this.category = (ExpenseCategory) category;
    }
    
    public ExpenseCategory getCategory() {
        return this.category;
    }
          
    public double getTotalValue() {
        double value = 0;
        
        Iterator<Payment> itp = this.payments.iterator();
        while (itp.hasNext()) {
            Payment payment = itp.next();
            LocalDate today = LocalDate.now();
            if (payment.hasConcretized() && (payment.getDate().isBefore(today) || payment.getDate().isEqual(today))) {
                value += payment.getValue();
            }
        }           
        
        return value * - 1;
    }  
}
