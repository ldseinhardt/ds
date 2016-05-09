package financesapp.model;
 
import java.time.*;

public class TransferenceIn extends Transaction {
    
    Account fromAccount;
    double value;
    
    public TransferenceIn() {
        super(LocalDate.now(), "", "");
    }
    
    public TransferenceIn(Account fromAccount, double value, LocalDate date, String information) {
        super(date, "", information);
        this.fromAccount = fromAccount;
        this.value = value;
    }
    
    public Account getFromAccount() {
        return this.fromAccount;
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
        return new IncomeCategory("");
    }
    
}
