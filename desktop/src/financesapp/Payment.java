package financesapp;

import java.time.LocalDate;

public class Payment {
    
    // Valor
    private double value;
    
    //Data
    private LocalDate date;
    
    //Pagamento contretizado (Pago/Recebido)
    private boolean concretized;   
    
    public Payment(double value, LocalDate date, boolean concretized) {
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
