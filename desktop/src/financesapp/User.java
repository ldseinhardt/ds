package financesapp;

import java.time.LocalDate;
import java.util.*;
import org.json.*;

public class User extends Observable {
    
    //Nome
    private String name;
    
    //Lista de contas
    private ArrayList<Account> accounts;
    
    public User() {
        this.name = "";
        this.accounts = new ArrayList();        
    }
    
    public void setName(String name) {
        this.name = name;
    }
        
    public void addAccount(Account account) {
        this.accounts.add(account);
    }
    
    public void update() {
        this.setChanged();
        this.notifyObservers();         
    }
    
    public void loadFromJSONString(String str) {
        try {
            accounts.clear();
            
            JSONObject json = new JSONObject(str);

            this.name = json.getString("name");

            Iterator<Object> ita = json.getJSONArray("accounts").iterator();
            while (ita.hasNext()) {
                JSONObject accountJSON = (JSONObject) ita.next();
                        
                Account account = new DefaultAccount(
                    accountJSON.getString("name"),
                    accountJSON.getDouble("openingBalance")
                );

                Iterator<Object> itt = accountJSON.getJSONArray("transactions").iterator();
                while (itt.hasNext()) {
                    JSONObject transactionJSON = (JSONObject) itt.next();

                    String categoryName = transactionJSON.getString("category");
                    
                    Transaction transaction = null;

                    if (transactionJSON.getString("type").equalsIgnoreCase("Expense")) {
                        transaction = new Expense();
                        transaction.setCategory(new ExpenseCategory(categoryName));
                    } else if (transactionJSON.getString("type").equalsIgnoreCase("Income")) {
                        transaction = new Income();    
                        transaction.setCategory(new IncomeCategory(categoryName));
                    }
                    
                    if (transaction != null) {
                        transaction.setDate(LocalDate.parse(transactionJSON.getString("date")));
                        transaction.setDescription(transactionJSON.getString("description"));
                        transaction.setInformation(transactionJSON.getString("information"));
                        
                        Iterator<Object> itp = transactionJSON.getJSONArray("payments").iterator();
                        while (itp.hasNext()) {
                            JSONObject paymentJSON = (JSONObject) itp.next();
                            transaction.addPayment(new Payment(
                                transaction,
                                paymentJSON.getDouble("value"),
                                LocalDate.parse(paymentJSON.getString("date")),
                                paymentJSON.getBoolean("concretized")
                            ));
                        }
                        
                        account.addTransaction(transaction);
                    }
                }            

                this.accounts.add(account);
            }            
        } catch(Exception e) {
            //algum atributo não foi encontrado
        }
        this.update();    
    }
    
    public String toJSONString() {
        JSONObject JSON = new JSONObject();
        
        JSON.put("name", this.name);
        
        JSONArray accountsJSON = new JSONArray();
        
        Iterator<Account> ita = this.accounts.iterator();
        while (ita.hasNext()) {
            Account account = ita.next();                                       
            
            JSONObject accountJSON = new JSONObject();
            
            accountJSON.put("type", account.getClass().getSimpleName());        
            accountJSON.put("name", account.getName());
            accountJSON.put("openingBalance", account.getOpeningBalance());
            
            JSONArray transactionsJSON = new JSONArray();
        
            Iterator<Transaction> itt = account.getTransactions().iterator();
            while (itt.hasNext()) {
                Transaction transaction = itt.next();                                       
            
                JSONObject transactionJSON = new JSONObject();
                
                transactionJSON.put("type", transaction.getClass().getSimpleName());
                transactionJSON.put("date", transaction.getDate().toString());
                transactionJSON.put("description", transaction.getDescription());
                transactionJSON.put("information", transaction.getInformation());
                
                Category category = transaction.getCategory();
                String categoryName = "";
                
                if (category != null) {
                    categoryName = category.getName();
                }
                
                transactionJSON.put("category", categoryName);
                
                JSONArray paymentsJSON = new JSONArray();
                
                Iterator<Payment> itp = transaction.getPayments().iterator();
                while (itp.hasNext()) {
                    Payment payment = itp.next();                                       
                    JSONObject paymentJSON = new JSONObject();                
                    paymentJSON.put("value", payment.getValue());
                    paymentJSON.put("date", payment.getDate().toString());
                    paymentJSON.put("concretized", payment.hasConcretized());                    
                    paymentsJSON.put(paymentJSON);
                }
                
                transactionJSON.put("payments", paymentsJSON);
                
                transactionsJSON.put(transactionJSON);
            }
            
            accountJSON.put("transactions", transactionsJSON);
            
            accountsJSON.put(accountJSON);
        } 
        
        JSON.put("accounts", accountsJSON);

        return JSON.toString(2);
    }
    
    public String getName() {
        return this.name;
    }
        
    public double getBalance() {
        double balance = 0;
       
        Iterator<Account> it = this.accounts.iterator();
        while (it.hasNext()) {
            Account account = it.next();                  
            
            balance += account.getBalance();
        }        
        
        return balance;
    }
    
    public double getBalance(LocalDate untilDate) {
        double balance = 0;
       
        Iterator<Account> it = this.accounts.iterator();
        while (it.hasNext()) {
            Account account = it.next();                  
            
            balance += account.getBalance(untilDate);
        }
        return balance;
    }
    
    public double getGeneralMaxBalance(int month, int year){
        double genMaxBalance = 0;
        
        Iterator<Account> it = this.accounts.iterator();
        while (it.hasNext()) {
            Account account = it.next();
            
            double maxBal = Math.abs(account.getMaxBalance(month, year));
            
            if(maxBal > genMaxBalance){
                genMaxBalance = maxBal;
            }
        }
        return genMaxBalance;
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
    
    public double getTotalByCategory(Category categ){
        double total = 0.0;
        
        for (Account account : accounts) {
            total += account.getTotalByCategory(categ);
        }
        return total;
    }
}
