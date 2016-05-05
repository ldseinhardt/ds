package financesapp;
 
import java.time.LocalDate;
import java.util.*;

public abstract class Transaction {
    
    // Conta a qual pertence
    private Account account;
    
    //Data
    protected LocalDate date;
    
    //Descrição
    protected String description;
    
    //Informação adicional
    protected String information;
    
    //Lista de pagamentos
    protected ArrayList<Payment> payments;
        
    public Transaction(LocalDate date, String description, String information) {
        this.account = null;
        this.date = date;
        this.description = description;
        this.information = information;
        this.payments = new ArrayList(); 
    }
    
    public void setAccount(Account account) {
        this.account = account;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setInformation(String information) {
        this.information = information;
    }
    
    public void addPayments(double value, LocalDate date, boolean concretized) {
        this.addPayments(value, date, concretized, 1);
    }
    
    public void addPayments(double value, LocalDate date, boolean concretized, int number) {        
        for (int i = 0; i < number; i++) {
            this.addPayment(new Payment(value, date.plusMonths(i), concretized));
        }
    }
        
    public void addPayment(Payment payment) {
        payment.setTransaction(this);
        this.payments.add(payment);
    }
    
    public abstract void setCategory(Category category);
    
    public abstract Category getCategory();
    
    public abstract double getTotalValue();
    
    public Account getAccount() {
        return this.account;
    }
    
    public LocalDate getDate() {
        return this.date;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getInformation() {
        return this.information;
    }
   
    public ArrayList<Payment> getPayments() {        
        return this.payments;
    }   
    
}
