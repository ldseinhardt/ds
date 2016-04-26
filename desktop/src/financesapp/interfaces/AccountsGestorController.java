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
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.util.Callback;

public class AccountsGestorController implements Initializable, Observer {
    
    //Tabela de contas
    private TableView<Account> tableView;
    
    //Referência da classe principal
    private FinancesApp app;
    
    ObservableList<Account> accounts;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private void onAdd() {        
        //Test
        this.app.getUser().addAccount(new DefaultAccount("Teste", 200));                
    }
    
    @FXML
    private void onEdit() {
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
    
    @FXML
    private void onDelete() {
        
    }  
    
    public void init(FinancesApp app) {
        this.app = app;
        this.update(null, null);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
        borderPane.setBottom(null);
        GridPane gridPane = new GridPane();
        gridPane.setPrefWidth(290);
        gridPane.setPrefHeight(83);
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(5, 5, 5, 5));
        
        ColumnConstraints cc = new ColumnConstraints();
        cc.setHalignment(HPos.RIGHT);
        cc.setMinWidth(125);
        cc.setMaxWidth(125);
        cc.setPrefWidth(125);
                        
        gridPane.getColumnConstraints().add(cc);
        
        gridPane.add(new Label("Nome da conta:"), 0, 0);
        gridPane.add(new Label("Saldo inicial:"), 0, 1);
        gridPane.add(new TextField(), 1, 0);
        gridPane.add(new TextField(), 1, 1);
        gridPane.add(new HBox(), 2, 2);
        
        borderPane.setBottom(gridPane);
        */
                
        tableView = new TableView();        
        tableView.setMinWidth(302);
        tableView.setMaxWidth(302);
        tableView.setPrefWidth(302);
        tableView.setPrefHeight(400);
        
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
        
        accounts = FXCollections.observableArrayList();
        
        tableView.getColumns().addAll(accountColunm, balanceColunm);
        tableView.setItems(accounts);
        
        borderPane.setCenter(tableView);        
        borderPane.setFocusTraversable(true);
    }  

    @Override
    public void update(Observable o, Object arg) {        
        accounts.setAll(this.app.getUser().getAccounts());
    }
}
