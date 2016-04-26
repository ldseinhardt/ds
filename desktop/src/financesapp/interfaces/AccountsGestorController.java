package financesapp.interfaces;

import financesapp.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.util.Callback;

public class AccountsGestorController implements Initializable {
    
    //Tabela de contas
    private TableView<Account> table;
    
    //Referência da classe principal
    private FinancesApp app;
    
    @FXML
    private BorderPane container;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table = new TableView<Account>();        
        table.setMinWidth(302);
        table.setMaxWidth(302);
        table.setPrefWidth(302);
        table.setPrefHeight(400);
        
        TableColumn accountColunm = new TableColumn("Conta");
        accountColunm.setMinWidth(200);
        accountColunm.setMaxWidth(200);
        accountColunm.setPrefWidth(200);
        accountColunm.setCellValueFactory(
            new PropertyValueFactory<Account, String>("name")
        );
        
        TableColumn balanceColunm = new TableColumn("Saldo");
        balanceColunm.setMinWidth(100);
        balanceColunm.setMaxWidth(100);
        balanceColunm.setPrefWidth(100);
        balanceColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Account, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Account, String> account) {
                SimpleStringProperty property = new SimpleStringProperty();
                NumberFormat formatter = NumberFormat.getCurrencyInstance(); 
                property.setValue(formatter.format(account.getValue().getBalance()));
                return property;      
            }
        });
        
        table.getColumns().addAll(accountColunm, balanceColunm);
        
        container.setCenter(table);
        
        container.setFocusTraversable(true);
    }    
    
    public void init(FinancesApp app) {
        this.app = app;
        
        ObservableList<Account> data = FXCollections.observableArrayList(
            this.app.getUser().getAccounts()
        );
        
        table.setItems(data);
    }
    
    public void onAdd() {
                
    }
    
    public void onEdit() {
        
    }
    
    public void onDelete() {
        
    }
}
