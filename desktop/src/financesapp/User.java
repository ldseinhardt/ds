package financesapp;

import java.util.ArrayList;

public class User {
    
    //Nome
    private String name;
    
    //Lista de contas
    private ArrayList<Account> accounts;
    
    public User(String name) {
        this.name = name;
        this.accounts = new ArrayList();
    }
    
    public void setName(String name) {
        this.name = name;
    }
        
    public void addAccount(Account account) {
        this.accounts.add(account);
    }
    
    public void deleteAccount(String name) {
        for(int i = 0, n = this.accounts.size(); i < n; i++) {
            if (this.accounts.get(i).getName().equalsIgnoreCase(name)) {
                this.accounts.remove(i);
            }
        }        
    }
    
    public String getName() {
        return this.name;
    }
        
    public float getBalanceTotal() {
        float balance = 0;
        
        for(int i = 0, n = this.accounts.size(); i < n; i++) {
            balance += this.accounts.get(i).getBalanceTotal();
        }
        
        return balance;
    }
   
     public Account getAccount(String name) {
        for(int i = 0, n = this.accounts.size(); i < n; i++) {
            if (this.accounts.get(i).getName().equalsIgnoreCase(name)) {
                return this.accounts.get(i);
            }
        }     
        
        return null;
    }   
    
}
