package financesapp;
 
import java.time.LocalDate;
import java.util.*;

abstract class Transaction {
    
    //Data
    protected LocalDate date;
    
    //Descrição
    protected String description;
    
    //Informação adicional
    protected String information;
    
    //Lista de pagamentos
    protected ArrayList<Payment> payments;
    
    public Transaction(LocalDate date, String description, String information) {
        this.date = date;
        this.description = description;
        this.information = information;
        this.payments = new ArrayList(); 
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
        int periodicity = 30;
        
        for (int i = 0; i < number; i++) {
            this.payments.add(new Payment(value, date.minusDays(i * periodicity), concretized));
        }
    }
        
    public void addPayment(Payment payment) {
        this.payments.add(payment);
    }
    
    public void deletePayment(int i) {
        this.payments.remove(i); 
    }
    
    abstract void setCategory(Category category);
    
    abstract Category getCategory();
    
    abstract double getTotalValue();
    
    public LocalDate getDate() {
        return this.date;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getInformation() {
        return this.information;
    }
   
    public Payment getPayment(int i) {
        if (i < this.payments.size()) {
            return this.payments.get(i);
        }     
        
        return null;
    }   
   
    public ArrayList<Payment> getPayments() {        
        return this.payments;
    }   
    
}
