package financesapp;

import java.util.ArrayList;

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
    
    public void deleteTransaction(String description) {
        for(int i = 0, n = this.transactions.size(); i < n; i++) {
            if (this.transactions.get(i).getDescription().equalsIgnoreCase(name)) {
                this.transactions.remove(i);
            }
        }        
    }
    
    public String getName() {
        return this.name;
    }
    
    public double getBalanceInitial() {
        return this.balanceInitial;
    }
    
    public double getBalanceTotal() {
        double balance = this.balanceInitial;
        
        for(int i = 0, n = this.transactions.size(); i < n; i++) {
            balance += this.transactions.get(i).getRealValue();
        }
        
        return balance;
    }
    
    public Transaction getTransaction(String description) {
        for(int i = 0, n = this.transactions.size(); i < n; i++) {
            if (this.transactions.get(i).getDescription().equalsIgnoreCase(name)) {
                return this.transactions.get(i);
            }
        }   
        
        return null;
    }
   
     public ArrayList<Transaction> getTransactions() {        
        return this.transactions;
    } 

}
