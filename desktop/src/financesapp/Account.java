package financesapp;

import java.util.*;

public abstract class Account {  
    
    //Nome da conta
    protected String name;
    
    // Saldo inicial da conta
    protected double openingBalance;
    
    //Maior saldo já obtido (por enquanto geral, depois no mês corrente)
    protected double maxBalance;
    
    // Lista de transações
    protected ArrayList<Transaction> transactions;

    public Account(String name, double openingBalance) {
        this.name = name;
        this.openingBalance = openingBalance;
        this.maxBalance = openingBalance;
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
        
        int currentMonth = 1 + Calendar.getInstance().get(Calendar.MONTH);
        for(Payment p : transaction.getPayments()){
            if(p.getDate().getMonthValue() == currentMonth)
                if(p.getValue() > 0 &&
                   p.hasConcretized()){ //ver como atualizar quando editar
                    maxBalance += p.getValue();
                    //System.out.println(maxBalance);
                }
        }
    }
    
    public void deleteTransaction(int i) {
        int currentMonth = 1 + Calendar.getInstance().get(Calendar.MONTH);
        for(Payment p : transactions.get(i).getPayments()){
            if(p.getDate().getMonthValue() == currentMonth)
                if(p.getValue() > 0 &&
                   p.hasConcretized()) //ver como atualizar quando editar
                    maxBalance -= p.getValue();
        }
        this.transactions.remove(i);
    }
    
    public String getName() {
        return this.name;
    }
    
    public double getOpeningBalance() {
        return this.openingBalance;
    }
    
    public double getMaxBalance() {
        return this.maxBalance;
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
    
    public Transaction getTransaction(int i) {
        if (i < this.transactions.size()) {
            return this.transactions.get(i);
        } 
        
        return null;
    }
   
    public ArrayList<Transaction> getTransactions() {        
        return this.transactions;
    }
    
    public double getTotalByCategory(Category categ){
        double total = 0.0;
        
        for (Transaction transaction : transactions) {
            if(transaction.getCategory() == categ){
                total += transaction.getPayments().get(0).getValue();
                //Por enquanto pegando so o primeiro pagamento.
            }
        }
        return total;
    }
}
