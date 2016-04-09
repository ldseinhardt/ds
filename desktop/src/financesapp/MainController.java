package financesapp;

import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.converter.FormatStringConverter;

public class MainController implements Initializable {
    
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

    protected void save() {
        System.out.println("save");
    }
}
