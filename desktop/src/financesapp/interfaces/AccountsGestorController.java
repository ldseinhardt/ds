package financesapp.interfaces;

import financesapp.*;
import java.net.URL;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.util.Callback;

public class AccountsGestorController implements Initializable, Observer {
    
    //Tabela de contas
    private TableView<Account> table;
    
    //Referência da classe principal
    private FinancesApp app;
    
    ObservableList<Account> accounts;
    
    @FXML
    private BorderPane container;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        table = new TableView();        
        table.setMinWidth(302);
        table.setMaxWidth(302);
        table.setPrefWidth(302);
        table.setPrefHeight(400);
        
        TableColumn accountColunm = new TableColumn("Conta");
        accountColunm.setMinWidth(200);
        accountColunm.setMaxWidth(200);
        accountColunm.setPrefWidth(200);
        accountColunm.setCellValueFactory(
            new PropertyValueFactory("name")
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
        
        accounts = FXCollections.observableArrayList();
        
        table.setItems(accounts);
    }    
    
    public void init(FinancesApp app) {
        this.app = app;  
        
        accounts.setAll(this.app.getUser().getAccounts());        
    }
    
    public void onAdd() {        
        //Test
        this.app.getUser().addAccount(new DefaultAccount("Teste", 200));                
    }
    
    public void onEdit() {
        //Test
        Account account = this.app.getUser().getAccount(5);
        if (account != null) {
            account.setName("Conta de teste");
            Income income = new Income();
            income.setDescription("Compras de supermercado");
            income.setInformation("blah... blah... blah...");
            income.addPayments(10, income.getDate(), true, 5);
            account.addTransaction(income);
            this.app.getUser().update();            
        }
    }
    
    public void onDelete() {
        
    }

    @Override
    public void update(Observable o, Object arg) {        
        accounts.setAll(this.app.getUser().getAccounts());
    }
}
