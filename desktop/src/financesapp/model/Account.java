package financesapp.model;

import java.time.*;
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
        transaction.setAccount(this);
        transactions.add(transaction);
    }
    
    public String getName() {
        return this.name;
    }
    
    public double getOpeningBalance() {
        return this.openingBalance;
    }
    
    public double getMaxBalance(int month, int year) {
        //Saldo no 1º dia deste mês
        double maxBalance = this.getBalance(LocalDate.of(year, month, 1));
        
        for (Payment payment : this.getPayments(month, year)) {
            if (payment.getValue() > 0 && payment.hasConcretized()) {
                maxBalance += payment.getValue();
            }
        }
        return maxBalance;
    }
    
    public double getBalance() {
        double balance = this.openingBalance;
        
        for (Transaction transaction : this.getTransactions()) {
            balance += transaction.getTotalValue();  
        }
        
        return balance;
    }
    
    public double getBalance(LocalDate untilDate) {
        double balance = this.openingBalance;
        
        for (Transaction transaction : this.getTransactions()) {            
            for (Payment payment : transaction.getPayments()) {
                if (payment.getDate().isBefore(untilDate.plusDays(1))) {
                    if (payment.hasConcretized()) {
                        if (payment.getTransaction().getClass().getSimpleName().equals("Income"))
                            balance += payment.getValue();
                        else //Expense
                            balance -= payment.getValue();
                    }
                }
            }
        }
        
        return balance;
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
        
        for (Transaction transaction : this.getTransactions()) {
            for (Payment payment : transaction.getPayments()) {
                if (payment.getDate().isAfter(initialDate.minusDays(1)) &&
                   payment.getDate().isBefore(finalDate.plusDays(1))) {
                    payments.add(payment);
                }
            }
        }
        return payments;
    }
    
    public double getTotalByCategory(Category categ, String type) {
        double total = 0;
        
        for (Transaction transaction : this.getTransactions()) {
            if (transaction.getClass().getSimpleName().equals(type)) {
                if (transaction.getCategory().getName().equals(categ.getName())) {
                    total += transaction.getPayments().get(0).getValue();
                    //Por enquanto pegando so o primeiro pagamento.
                }
            }
        }
        return total;
    }
    
}
