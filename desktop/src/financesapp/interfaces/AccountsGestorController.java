package financesapp.interfaces;

import financesapp.*;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.util.Callback;

public class AccountsGestorController implements Initializable, Observer {
    
    //Tabela de contas
    private TableView<Account> tableView;
    
    //Referências para o formulário
    private Parent form;
    private AccountsGestorFormController formController;
    
    //Referência da classe principal
    private FinancesApp app;
    
    ObservableList<Account> accounts;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private void onAdd() {  
        if (this.form != null && this.formController != null) {  
            this.formController.setAccount(null);
            this.borderPane.setBottom(this.form);
        }              
    }
    
    @FXML
    private void onEdit() {
        Account account = this.tableView.getSelectionModel().getSelectedItem();
        if (this.form != null && this.formController != null && account != null) {
            this.formController.setAccount(account);        
            this.borderPane.setBottom(this.form);    
        }
    }
    
    @FXML
    private void onDelete() {
        Account account = this.tableView.getSelectionModel().getSelectedItem();
        if (account != null) {
            this.app.getUser().getAccounts().remove(account);
            this.app.getUser().update();
        }    
    }  
    
    public void init(FinancesApp app) {
        this.app = app;
        this.formController.init(this.app);
        this.update(null, null);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {                
        this.tableView = new TableView();        
        this.tableView.setMinWidth(302);
        this.tableView.setMaxWidth(302);
        this.tableView.setPrefWidth(302);
        this.tableView.setPrefHeight(400);
        
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
        
        this.accounts = FXCollections.observableArrayList();
        
        this.tableView.getColumns().addAll(accountColunm, balanceColunm);
        this.tableView.setItems(this.accounts);
        
        this.borderPane.setCenter(this.tableView);        
        this.borderPane.setFocusTraversable(true);
                
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("AccountsGestorFormView.fxml")
            );
            this.form = loader.load();
            this.formController = loader.getController();
        } catch(Exception e) {
            this.form = null;
            this.formController = null;
        }
    }  

    @Override
    public void update(Observable o, Object arg) {        
        this.accounts.setAll(this.app.getUser().getAccounts());
    }
}