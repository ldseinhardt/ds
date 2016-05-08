package financesapp.model;
 
import java.time.*;

public class Income extends Transaction {
 
    //Categoria
    private IncomeCategory category;

    public Income() {
        super(LocalDate.now(), "", "");
        this.category = null;
    }    
    
    public Income(LocalDate date, String description, String information) {
        super(date, description, information);
        this.category = null;
    }
    
    public void setCategory(Category category) {
        this.category = (IncomeCategory) category;
    }
    
    public IncomeCategory getCategory() {
        return this.category;
    }
    
    public double getTotalValue() {
        double value = 0;
        
        LocalDate today = LocalDate.now();
        
        for (Payment payment : this.getPayments()) {
            if (payment.hasConcretized() && (payment.getDate().isBefore(today) || payment.getDate().isEqual(today))) {
                value += payment.getValue();
            }
        }     
        
        return value;
    }
    
}
