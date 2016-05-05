package financesapp;

import java.time.LocalDate;
import java.util.*;

public abstract class Account {  
    
    //Nome da conta
    protected String name;
    
    // Saldo inicial da conta
    protected double openingBalance;
    
    // Lista de transações
    protected ArrayList<Transaction> transactions;

    public Account(String name, double openingBalance) {
        this.name = name;
        this.openingBalance = openingBalance;
        this.transactions = new ArrayList();
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setOpeningBalance(double openingBalance) {
        this.openingBalance = openingBalance;
    }    
    
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
    
    public void deleteTransaction(int i) {
        this.transactions.remove(i);
    }
    
    public String getName() {
        return this.name;
    }
    
    public double getOpeningBalance() {
        return this.openingBalance;
    }
    
    public double getMaxBalance(int month, int year) {
        //Saldo no 1º dia deste mês
        double maxBalance = this.getBalance(LocalDate.of(
            year, month, 1)
        );
        
        for(Payment p : getPayments(month, year)){
            if(p.getValue() > 0 &&
               p.hasConcretized()){
                maxBalance += p.getValue();
            }
        }
        return maxBalance;
    }
    
    public double getBalance() {
        double balance = this.openingBalance;
        
        Iterator<Transaction> it = this.transactions.iterator();
        while (it.hasNext()) {
            Transaction transaction = it.next();                  
            
            balance += transaction.getTotalValue();
        }
        
        return balance;
    }
    
    public double getBalance(LocalDate untilDate) {
        double balance = this.openingBalance;
        
        Iterator<Transaction> it = this.transactions.iterator();
        while (it.hasNext()) {
            Transaction transaction = it.next();
            
            for(Payment payment : transaction.getPayments()){
                if(payment.getDate().isBefore(untilDate.plusDays(1))){
                    if(payment.hasConcretized()){
                        if(payment.getTransaction().getType().equals("Income"))
                            balance += payment.getValue();
                        else //Expense
                            balance -= payment.getValue();
                    }
                }
            }
        }
        return balance;
    }
    
    public Transaction getTransaction(int i) {
        if (i < this.transactions.size()) {
            return this.transactions.get(i);
        } 
        
        return null;
    }
   
    public ArrayList<Transaction> getTransactions() {        
        return this.transactions;
    }
    
    public ArrayList<Payment> getPayments(int month, int year) {
        ArrayList<Payment> payments = new ArrayList();
        
        Calendar last = Calendar.getInstance();
        last.set(Calendar.YEAR, year);
        last.set(Calendar.MONTH, month-1); //Calendar: mês de 0 a 11
        last.set(Calendar.DAY_OF_MONTH, last.getActualMaximum(Calendar.DAY_OF_MONTH));
        
        LocalDate initialDate = LocalDate.of(year, month, 1);
        LocalDate finalDate   = LocalDate.of(year, month,
            last.get(Calendar.DAY_OF_MONTH)
        );
        
        for(Transaction trans : this.transactions){
            for(Payment paym : trans.getPayments()){
                if(paym.getDate().isAfter(initialDate.minusDays(1)) &&
                   paym.getDate().isBefore(finalDate.plusDays(1))){
                    payments.add(paym);
                }
            }
        }
        return payments;
    }
    
    public double getTotalByCategory(Category categ){
        double total = 0.0;
        
        for (Transaction transaction : transactions) {
            if(transaction.getCategory().getName().equals(categ.getName())){
                total += transaction.getPayment(0).getValue();
                //Por enquanto pegando so o primeiro pagamento.
            }
        }
        return total;
    }
}
