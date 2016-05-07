package financesapp.controller;

import financesapp.*;
import financesapp.model.*;
import java.net.URL;
import java.text.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;
import javafx.util.*;

public class AccountsGestor implements Initializable, Observer {
    
    //Tabela de contas
    private TableView<Account> tableView;
    
    //Referências para o formulário
    private Parent form;
    private AccountsGestorForm formController;
    
    //Referência da classe principal
    private FinancesApp app;
    
    ObservableList<Account> accounts;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private void onAdd() {  
        if (this.form != null && this.formController != null) {  
            this.borderPane.setBottom(this.form);
            this.formController.setAccount(null);
        }              
    }
    
    @FXML
    private void onEdit() {
        this.edit(this.tableView.getSelectionModel().getSelectedItem());
    }
    
    @FXML
    private void onDelete() {
        Account account = this.tableView.getSelectionModel().getSelectedItem();
        if (account != null) {
            this.app.getUser().getAccounts().remove(account);
            if (this.app.getUser().getAccounts().isEmpty()) {
                this.app.getUser().addAccount(new DefaultAccount(this.app.getDefaultAccountName()));                
            }
            this.app.getUser().update();
        }    
    }  
    private void edit(Account account) {
        if (this.form != null && this.formController != null && account != null) {      
            this.borderPane.setBottom(this.form);   
            this.formController.setAccount(account);   
        }
    }
    
    public void init(FinancesApp app) {
        this.app = app;
        this.app.getUser().addObserver(this);
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
        accountColunm.setPrefWidth(200);
        accountColunm.setCellValueFactory(new PropertyValueFactory("name"));
        
        TableColumn balanceColunm = new TableColumn("Saldo");
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
        
        this.tableView.setRowFactory(tv -> {
            TableRow<Account> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && (!row.isEmpty()) ) {
                    Account account = row.getItem();
                    this.edit(account);
                }
            });
            return row;
        });
        
        this.accounts = FXCollections.observableArrayList();
        
        this.tableView.getColumns().addAll(accountColunm, balanceColunm);
        this.tableView.setItems(this.accounts);
                
        ContextMenu contextMenu = new ContextMenu();

        MenuItem addOption = new MenuItem("Adicionar");
        addOption.setOnAction(e -> this.onAdd());

        MenuItem editOption = new MenuItem("Editar");
        editOption.setOnAction(e -> this.onEdit());
        
        MenuItem deleteOption = new MenuItem("Remover");
        deleteOption.setOnAction(e -> this.onDelete());
        
        contextMenu.getItems().addAll(addOption, editOption, deleteOption);
        
        this.tableView.setContextMenu(contextMenu);        
        
        this.borderPane.setCenter(this.tableView);   
                
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/financesapp/view/AccountsGestorForm.fxml")
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
