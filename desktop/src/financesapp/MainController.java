package financesapp;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.util.converter.FormatStringConverter;

public class MainController implements Initializable {
    
    //Usuário
    private User user;
    
    //Categorias de despesas
    private ArrayList<ExpenseCategory> expenseCategories;
    
    //Categorias de receitas
    private ArrayList<IncomeCategory> incomeCategories;
    
    //Nome padrão do arquivo
    private final String data_file = "data_user.json"; 
    
    @FXML
    private DatePicker filterInitialDate;
    
    @FXML
    private DatePicker filterFinalDate;
    
    @FXML
    private ComboBox filterAccount;
    
    @FXML
    private ComboBox filterType;
    
    @FXML
    private ComboBox filterCategory;
    
    @FXML
    private DatePicker date;
    
    @FXML
    private TextField value;
    
    @FXML
    private ComboBox category;
    
    @FXML
    private ComboBox account;
    
    @FXML
    private Label balance;
    
    private final LocalDate today = LocalDate.now();
    
    private final static ObservableList<String> accounts = FXCollections.observableArrayList();
    
    private final static ObservableList<String> types = FXCollections.observableArrayList();
    
    private final static ObservableList<String> categories = FXCollections.observableArrayList();
    
    static {        
        accounts.addAll(new String[] {
            "Conta carteira"
        });
        
        types.addAll(new String[] {
            "Despesas",
            "Receitas"
        });
        
        categories.addAll(new String[] {
            "Alimentos",
            "Outros"
        });
    }
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {     
        filterInitialDate.setValue(today.minusMonths(1));
        filterFinalDate.setValue(today);
        
        filterAccount.setItems(accounts);
        filterType.setItems(types);
        filterCategory.setItems(categories);
        
        date.setValue(today);
        
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();
        TextFormatter<Number> formatter = new TextFormatter<>(new FormatStringConverter<>(currencyInstance));
        formatter.valueProperty().bindBidirectional(new SimpleDoubleProperty(0));
        value.setTextFormatter(formatter);
        
        account.setItems(accounts);
        account.setValue(accounts.get(0));        
        
        category.setItems(categories);
    }
    
    public void test1() {
        this.user.setName("Usuário Padrão");
        System.out.println(user.getName());
        
        DefaultAccount carteira = new DefaultAccount("Conta Carteira");
        this.user.addAccount(carteira);
        
        Transaction transaction;
        
        transaction = new Income(today, "Serviço prestado", "");
        transaction.addPayments(75, today, true);
        transaction.setCategory(new IncomeCategory("Serviços"));
        carteira.addTransaction(transaction);

        transaction = new Expense(today, "Compra 1", "");
        transaction.addPayments(10, today.minusMonths(1), true, 5);
        carteira.addTransaction(transaction);
        
        transaction = new Expense(today, "Compra 2", "");
        transaction.addPayments(22.5, today, true);
        carteira.addTransaction(transaction);
        
        transaction = new Expense(today, "Compra 3", "");
        transaction.addPayments(100, today, true);
        carteira.addTransaction(transaction);
                
        System.out.println(carteira.getName());
        System.out.println(carteira.getBalance());            
            
        DefaultAccount caixa = new DefaultAccount("Conta Caixa", 3750.76);
        this.user.addAccount(caixa);
        
        System.out.println(caixa.getName());
        System.out.println(caixa.getBalance());
        
        System.out.println(this.user.getBalance()); 
        
        System.out.println(this.user.toJSONString());
        
        this.saveFile(this.data_file, this.user.toString());
    }
    
    public void test2() {
        this.user.loadFromJSONString(this.loadFile(this.data_file));
        System.out.println(this.user.getName());
        
        Iterator<Account> it = this.user.getAccounts().iterator();
        while (it.hasNext()) {
            Account account = it.next();                  
        
            System.out.println(account.getName());
            System.out.println(account.getBalance());   
        }        
        
        System.out.println(this.user.getBalance()); 
      
        System.out.println(this.user.toJSONString());  
    }
    
    public void load() {
        //Testes
        this.test1();
        //this.test2();
        
        NumberFormat formatter = NumberFormat.getCurrencyInstance();        
        this.balance.setText(formatter.format(this.user.getBalance()));
    }

    public void save() {
        this.saveFile(this.data_file, this.user.toJSONString());
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public void setExpenseCategories(ArrayList<ExpenseCategory> categories) {
        this.expenseCategories = categories;
    }
    
    public void setIncomeCategories(ArrayList<IncomeCategory> categories) {
        this.incomeCategories = categories;
    }
    
    public User getUser() {
        return this.user;
    }
    
    public ArrayList<ExpenseCategory> getExpenseCategories() {
        return this.expenseCategories;
    }
    
    public ArrayList<IncomeCategory> getIncomeCategories() {
        return this.incomeCategories;
    }
    
    public String loadFile(String filename) {
        // implementar leitura de arquivo
        return "{\"name\":\"Usuário Padrão\",\"accounts\":[{\"balanceInitial\":0,\"name\":\"Conta Carteira\",\"type\":\"DefaultAccount\",\"transactions\":[{\"date\":\"2016-04-13\",\"payments\":[{\"date\":\"2016-04-13\",\"concretized\":true,\"value\":75}],\"description\":\"Serviço prestado\",\"information\":\"\",\"type\":\"Income\",\"category\":\"Serviços\"},{\"date\":\"2016-04-13\",\"payments\":[{\"date\":\"2016-04-13\",\"concretized\":true,\"value\":10}],\"description\":\"Compra 1\",\"information\":\"\",\"type\":\"Expense\",\"category\":\"\"},{\"date\":\"2016-04-13\",\"payments\":[{\"date\":\"2016-04-13\",\"concretized\":true,\"value\":22.5}],\"description\":\"Compra 2\",\"information\":\"\",\"type\":\"Expense\",\"category\":\"\"},{\"date\":\"2016-04-13\",\"payments\":[{\"date\":\"2016-04-13\",\"concretized\":true,\"value\":100}],\"description\":\"Compra 3\",\"information\":\"\",\"type\":\"Expense\",\"category\":\"\"}]},{\"balanceInitial\":3750.76,\"name\":\"Conta Caixa\",\"type\":\"DefaultAccount\",\"transactions\":[]}]}";
    }
    
    public void saveFile(String filename, String content) {
        //implementar escrita de arquivo
    }
    
    public void copyFile(String a, String b) {
        //implementar copia de arquivo de a para b
    }
    
    public void exportData() {
        //local do arquivo selecionado pelo usuário
        String filename = "";
        
        this.copyFile(this.data_file, filename);        
    }
    
    public void importData() {
        //local do arquivo selecionado pelo usuário
        String filename = "";
        
        this.copyFile(filename, this.data_file);
        this.user = new User();
        this.user.loadFromJSONString(this.loadFile(this.data_file));
        
        //atualizar dados na interface
    }
    
}
