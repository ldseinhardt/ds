package financesapp;
 
import java.time.LocalDate;
import java.util.Iterator;

public class Recipe extends Transaction {
 
    //Categoria
    private RecipeCategory category;

    public Recipe() {
        super(LocalDate.now(), "", "");
        this.category = null;
    }    
    
    public Recipe(LocalDate date, String description, String information) {
        super(date, description, information);
        this.category = null;
    }
    
    public void setCategory(Category category) {
        this.category = (RecipeCategory) category;
    }
    
    public RecipeCategory getCategory() {
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
