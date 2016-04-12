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
    private ArrayList<RecipeCategory> recipeCategories;
    
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
        filterInitialDate.setValue(today.minusDays(30));
        filterFinalDate.setValue(today);
        
        filterAccount.setItems(accounts);
        filterType.setItems(types);
        filterCategory.setItems(categories);
        
        date.setValue(today);
        
        final NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();
        TextFormatter<Number> formatter = new TextFormatter<>(new FormatStringConverter<>(currencyInstance));
        formatter.valueProperty().bindBidirectional(new SimpleDoubleProperty(0));
        value.setTextFormatter(formatter);
        
        account.setItems(accounts);
        account.setValue(accounts.get(0));        
        
        category.setItems(categories);
      
    }
    
    public void load() {
        //Testes
        user.loadFromJSONString("{\"name\": \"Usuário padrão\"}");
        System.out.println(user.getName());
        System.out.println(user.toJSONString());
        
        DefaultAccount carteira = new DefaultAccount("Conta Carteira");
        user.addAccount(carteira);
        carteira.addTransaction(new Recipe(75, LocalDate.now(), 1, true, "Serviço prestado", ""));
        carteira.addTransaction(new Expense(010.0, LocalDate.now(), 1, true, "Compra 1", ""));
        carteira.addTransaction(new Expense(022.5, LocalDate.now(), 1, true, "Compra 2", ""));
        carteira.addTransaction(new Expense(100.0, LocalDate.now(), 1, true, "Compra 3", ""));
        System.out.println(carteira.getName());
        System.out.println(carteira.getBalanceTotal());
        
        
        DefaultAccount caixa = new DefaultAccount("Conta Caixa", 3750.76);
        user.addAccount(caixa);
        System.out.println(caixa.getName());
        System.out.println(caixa.getBalanceTotal());
        
        System.out.println(user.getBalanceTotal()); 
    }

    public void save() {
        System.out.println("save");
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public void setExpenseCategories(ArrayList<ExpenseCategory> categories) {
        this.expenseCategories = categories;
    }
    
    public void setRecipeCategories(ArrayList<RecipeCategory> categories) {
        this.recipeCategories = categories;
    }
    
    public User getUser() {
        return this.user;
    }
    
    public ArrayList<ExpenseCategory> getExpenseCategories() {
        return this.expenseCategories;
    }
    
    public ArrayList<RecipeCategory> getRecipeCategories() {
        return this.recipeCategories;
    }
    
}
