package financesapp;

import java.time.LocalDate;

public class Payment {
    
    // Transação a qual pertence
    private Transaction transaction;
    
    // Valor
    private double value;
    
    //Data
    private LocalDate date;
    
    //Pagamento contretizado (Pago/Recebido)
    private boolean concretized;   
    
    public Payment(Transaction transaction, double value, LocalDate date, boolean concretized) {
        this.transaction = transaction;
        this.value = value;
        this.date = date;
        this.concretized = concretized;
    }
    
    public void setValue(double value) {
        this.value = value;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setConcretized(boolean concretized) {
        this.concretized = concretized;
    }    
    
    public Transaction getTransaction() {
        return this.transaction;
    }
    
    public double getValue() {
        return this.value;
    }
    
    public LocalDate getDate() {
        return this.date;
    }
    
    public boolean hasConcretized() {
        return this.concretized;
    }
    
}
