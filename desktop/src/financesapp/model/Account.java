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
    
    public double getMaxBalance(Period period) {
        //Saldo no 1º dia deste mês
        double maxBalance = this.getBalance(period.getInitialDate());
        
        for (Payment payment : this.getPayments(period)) {
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
    
    public ArrayList<Payment> getPayments(Period period) {
        
        ArrayList<Payment> payments = new ArrayList();
        
        for (Transaction transaction : this.getTransactions()) {
            for (Payment payment : transaction.getPayments()) {
                if (payment.getDate().isAfter(period.getInitialDate().minusDays(1)) &&
                   payment.getDate().isBefore(period.getFinalDate().plusDays(1))) {
                    payments.add(payment);
                }
            }
        }
        
        return payments;
    }
    
    public double getTotalByCategory(
        Category categ, String type, Period period) {
        
        double total = 0;
        
        for (Transaction transaction : this.getTransactions()) {
            if (transaction.getClass().getSimpleName().equals(type)) {
                if (transaction.getCategory().getName().equals(categ.getName())) {
                    for (Payment payment : transaction.getPayments()){
                        if (payment.getDate().isAfter(period.getInitialDate().minusDays(1)) &&
                            payment.getDate().isBefore(period.getFinalDate().plusDays(1))) {
                                total += payment.getValue();
                        }
                    }
                }
            }
        }
        
        return total;
    }
    
}
