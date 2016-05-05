package financesapp;
 
import java.time.LocalDate;
import java.util.Iterator;

public class Income extends Transaction {
 
    //Categoria
    private IncomeCategory category;

    public Income() {
        super(LocalDate.now(), "", "");
        this.category = null;
        super.type = "Income";
    }    
    
    public Income(LocalDate date, String description, String information) {
        super(date, description, information);
        this.category = null;
        super.type = "Income";
    }
    
    public void setCategory(Category category) {
        this.category = (IncomeCategory) category;
    }
    
    public IncomeCategory getCategory() {
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
        
        return value;
    }
    
}
