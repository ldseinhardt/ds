package financesapp;

import java.time.LocalDate;
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
        //Falta adicionar informações de categorias
        try {
            JSONObject json = new JSONObject(str);

            this.name = json.getString("name");

            JSONArray accountsJSON = json.getJSONArray("accounts");

            for(int i = 0, n = accountsJSON.length(); i < n; i++) {
                JSONObject accountJSON = accountsJSON.getJSONObject(i);

                Account account = new DefaultAccount(
                    accountJSON.getString("name"),
                    accountJSON.getDouble("balanceInitial")
                );

                JSONArray transactionsJSON = accountJSON.getJSONArray("transactions");

                for(int j = 0, m = transactionsJSON.length(); j < m; j++) {
                    JSONObject transactionJSON = transactionsJSON.getJSONObject(j);                

                    Transaction transaction = null;

                    if (transactionJSON.getString("type").equalsIgnoreCase("Expense")) {
                        transaction = new Expense();
                    } else if (transactionJSON.getString("type").equalsIgnoreCase("Recipe")) {
                        transaction = new Recipe();    
                    }
                    
                    if (transaction != null) {
                        transaction.setValue(transactionJSON.getDouble("value"));
                        transaction.setDate(LocalDate.parse(transactionJSON.getString("date")));
                        transaction.setNumber(transactionJSON.getInt("number"));
                        transaction.setConcretized(transactionJSON.getBoolean("concretized"));
                        transaction.setDescription(transactionJSON.getString("description"));
                        transaction.setInformation(transactionJSON.getString("information"));
                        
                        account.addTransaction(transaction);
                    }
                }            

                this.accounts.add(account);
            }            
        } catch(Exception e) {
            //algum atributo não foi encontrado
        }        
    }
    
    public String toJSONString() {
        //Falta adicionar informações de categorias
        JSONObject JSON = new JSONObject();
        
        JSON.put("name", this.name);
        
        JSONArray accountsJSON = new JSONArray();
        
        for(int i = 0, n = this.accounts.size(); i < n; i++) {
            Account account = this.accounts.get(i);
            
            JSONObject accountJSON = new JSONObject();
            
            accountJSON.put("type", account.getClass().getSimpleName());        
            accountJSON.put("name", account.getName());
            accountJSON.put("balanceInitial", account.getBalanceInitial());
            
            JSONArray transactionsJSON = new JSONArray();
        
            ArrayList<Transaction> transactions = account.getTransactions();
                
            for(int j = 0, m = transactions.size(); j < m; j++) {
                Transaction transaction = transactions.get(j);
            
                JSONObject transactionJSON = new JSONObject();
                
                transactionJSON.put("type", transaction.getClass().getSimpleName());
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
   
    public Account getAccount(int i) {
        if (i < this.accounts.size()) {
            return this.accounts.get(i);
        }     
        
        return null;
    }   
   
    public ArrayList<Account> getAccounts() {        
        return this.accounts;
    }   
    
}
