package financesapp;

import java.util.*;
import org.json.*;

public class User {
    
    //Nome
    private String name;
    
    //Lista de contas
    private ArrayList<Account> accounts;
    
    public User() {
        this.name = "";
        this.accounts = new ArrayList();        
    }
    
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
    
    public void loadFromJSONString(String str) {
        JSONObject json = new JSONObject(str);
        this.name = json.getString("name");
    }
    
    public String toJSONString() {
        JSONObject JSON = new JSONObject();
        
        JSON.put("name", this.name);
        
        JSONArray accountsJSON = new JSONArray();
        
        for(int i = 0, n = this.accounts.size(); i < n; i++) {
            Account account = this.accounts.get(i);
            
            JSONObject accountJSON = new JSONObject();
            
            accountJSON.put("name", account.getName());
            
            accountJSON.put("balanceInitial", account.getBalanceInitial());
            
            JSONArray transactionsJSON = new JSONArray();
        
            ArrayList<Transaction> transactions = account.getTransactions();
                
            for(int j = 0, m = transactions.size(); j < m; j++) {
                Transaction transaction = transactions.get(j);
            
                JSONObject transactionJSON = new JSONObject();
                
                transactionJSON.put("date", transaction.getDate().toString());
                transactionJSON.put("value", transaction.getValue());
                transactionJSON.put("number", transaction.getNumber());
                transactionJSON.put("concretized", transaction.hasConcretized());
                transactionJSON.put("description", transaction.getDescription());
                transactionJSON.put("information", transaction.getInformation());
                
                transactionsJSON.put(transactionJSON);
            }
            
            accountJSON.put("transactions", transactionsJSON);
            
            accountsJSON.put(accountJSON);
        } 
        
        JSON.put("accounts", accountsJSON);

        return JSON.toString();
    }
    
    public String getName() {
        return this.name;
    }
        
    public double getBalanceTotal() {
        double balance = 0;
        
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
   
     public ArrayList<Account> getAccounts() {        
        return this.accounts;
    }   
    
}
