package financesapp;
 
import java.time.LocalDate;

abstract class Transaction {
    
    // Valor
    protected double value;
    
    //Data
    protected LocalDate date;
    
    //Número de vezes
    protected int number;
    
    //Transação contretizada (Pago/Recebido)
    protected boolean concretized;
    
    //Descrição
    protected String description;
    
    //Informação adicional
    protected String information;
    
    public Transaction(double value, LocalDate date, int number, boolean concretized, String description, String information) {
        this.value = Math.abs(value);
        this.date = date;
        this.number = number;
        this.concretized = concretized;
        this.description = description;
        this.information = information;
    }
    
    public void setValue(double value) {
        this.value = Math.abs(value);
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public void setNumber(int number) {
        this.number = number;
    }
    
    public void setConcretized(boolean concretized) {
        this.concretized = concretized;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public void setInformation(String information) {
        this.information = information;
    }
    
    abstract double getValue();
    
    public LocalDate getDate() {
        return this.date;
    }
    
    public int getNumber() {
        return this.number;
    }
    
    public boolean hasConcretized() {
        return this.concretized;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String getInformation() {
        return this.information;
    }
    
}
