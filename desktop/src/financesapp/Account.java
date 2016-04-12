package financesapp;

import java.util.ArrayList;

abstract class Account {  
    
    //Nome da conta
    protected String name;
    
    // Saldo da conta (calculado com as transações)
    protected double balance;
    
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
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public void setBalanceInitial(double balanceInitial) {
        this.balanceInitial = balanceInitial;
    }    
    
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        this.balance += transaction.getValue();
    }
    
    public String getName() {
        return this.name;
    }
    
    public double getBalance() {
        return this.balance;
    }
    
    public double getBalanceInitial() {
        return this.balanceInitial;
    }
    
    public double getBalanceTotal() {
        return this.balanceInitial + this.balance;
    }

}
