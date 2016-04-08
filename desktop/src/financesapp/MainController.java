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
    private DatePicker date;
    
    @FXML
    private TextField value;
    
    @FXML
    private ComboBox category;
    
    @FXML
    private ComboBox account;
    
    @FXML
    private ComboBox accountFilter;
    
    private final LocalDate today = LocalDate.now();
    
    private final static ObservableList<String> categories = FXCollections.observableArrayList();
    
    private final static ObservableList<String> accounts = FXCollections.observableArrayList();
    
    static {
        categories.addAll(new String[]{
            "Alimentos",
            "Outros"
        });
        accounts.addAll(new String[]{
            "Conta carteira"
        });
    }
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        date.setValue(today);
        
        final NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();
        String symbol = currencyInstance.getCurrency().getSymbol();
        TextFormatter<Number> formatter = new TextFormatter<>(new FormatStringConverter<>(currencyInstance));
        formatter.valueProperty().bindBidirectional(new SimpleDoubleProperty(0));
        value.setTextFormatter(formatter);
        value.setMaxSize(140, TextField.USE_COMPUTED_SIZE);
        
        category.setItems(categories);
        
        account.setItems(accounts);
        account.setValue(accounts.get(0));

        accountFilter.setItems(accounts);
        accountFilter.setValue(accounts.get(0));
    }

    protected void save() {
        System.out.println("save");
    }
}
