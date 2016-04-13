package financesapp;

import java.util.*;

abstract class Account {  
    
    //Nome da conta
    protected String name;
    
    // Saldo inicial da conta
    protected double balanceInitial;
    
    // Lista de transações
    protected ArrayList<Transaction> transactions;

    public Account(String name, double balanceInitial) {
        this.name = name;
        this.balanceInitial = balanceInitial;
        this.transactions = new ArrayList();
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setBalanceInitial(double balanceInitial) {
        this.balanceInitial = balanceInitial;
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
    
    public double getBalanceInitial() {
        return this.balanceInitial;
    }
    
    public double getBalance() {
        double balance = this.balanceInitial;
        
        Iterator<Transaction> it = this.transactions.iterator();
        while (it.hasNext()) {
            Transaction transaction = it.next();                  
            
            balance += transaction.getTotalValue();
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

}

