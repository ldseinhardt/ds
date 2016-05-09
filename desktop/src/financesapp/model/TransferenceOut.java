package financesapp.model;
 
import java.time.*;

public class TransferenceOut extends Transaction {
    
    Account toAccount;
    double value;
    
    public TransferenceOut() {
        super(LocalDate.now(), "", "");
    }
    
    public TransferenceOut(Account toAccount, double value, LocalDate date, String information) {
        super(date, "", information);
        this.toAccount = toAccount;
        this.value = value;
    }
    
    public Account gettoAccount() {
        return this.toAccount;
    }  
    
    public double getValue() {
        return this.value;
    }
    
    @Override
    public double getTotalValue() {
        return this.getValue();
    }
    
    @Override
    public void setCategory(Category category) {
    }
    
    @Override
    public Category getCategory() {
        return new ExpenseCategory("");
    }
    
}
