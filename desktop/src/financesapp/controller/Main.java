package financesapp.controller;

import financesapp.*;
import financesapp.model.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.time.format.*;
import java.util.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.*;

public class Main implements Initializable, Observer {
    
    //Referência da classe principal
    private FinancesApp app;
    
    //Referências para o relatório de contas
    private Parent accountsView;
    private Accounts accountsController;
    
    //Referências para o formulário
    private Parent form;
    private TransactionsForm formController;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private ScrollPane expScrollPane;
   
    @FXML
    private ScrollPane incScrollPane;
    
    //Tabela de Transações
    @FXML
    private TableView<Payment> tableView;
    
    @FXML
    private TableColumn dateColunm;    
    
    @FXML 
    private TableColumn descriptionColunm;
    
    @FXML 
    private TableColumn informationColunm;
    
    @FXML       
    private TableColumn valueColunm;
    
    @FXML
    private TableColumn concretizedColunm;
    
    @FXML
    private TableColumn accountColunm;
    
    @FXML
    private TableColumn typeColunm;
    
    @FXML
    private TableColumn categoryColunm;
    
    private ObservableList<Payment> transactions;   
    
    // Filtros
    private String accountFilter;
    private String typeFilter;
    private String categoryFilter;
    
    @FXML
    private Tab transactionsTab;
    
    @FXML
    private void onAddExpense() { 
        if (this.form != null && this.formController != null) { 
            this.showForm();
            this.formController.setTransaction(new Expense());
        }       
    }
    
    @FXML
    private void onAddIncome() {
        if (this.form != null && this.formController != null) { 
            this.showForm();     
            this.formController.setTransaction(new Income());   
        }    
    }
    
    @FXML
    private void onEditTransaction() {
        this.editTransaction(this.tableView.getSelectionModel().getSelectedItem());     
    }
    
    @FXML
    private void onDeleteTransaction() {
        Payment payment = this.tableView.getSelectionModel().getSelectedItem();
        if (payment != null) {
            Transaction transaction = payment.getTransaction();            
            transaction.getPayments().remove(payment);            
            if (transaction.getPayments().isEmpty()) {
                transaction.getAccount().getTransactions().remove(transaction);
            }            
            this.app.getUser().update();        
        }
    }
    
    @FXML
    private void onFilterTypeByExpense() {  
        this.typeFilter = Expense.class.getSimpleName();
        this.showTransactions();        
    }
    
    @FXML
    private void onFilterTypeByIncome() {
        this.typeFilter = Income.class.getSimpleName();
        this.showTransactions();               
    }
    
    @FXML
    private void onFilterTypeByNone() { 
        this.typeFilter = "";
        this.showTransactions();             
    }  
    
    private void editTransaction(Payment payment) {
        if (this.form != null && this.formController != null && payment != null) { 
            this.showForm();  
            this.formController.setTransaction(payment.getTransaction());    
        }       
    }
    
    private void showExpensesByCategory(Calendar initialDate, Calendar finalDate) {
        
        double max = 1000;//temporario
        
        VBox vBox = new VBox(8);
        this.expScrollPane.setContent(vBox);
        
        for (ExpenseCategory expCateg : this.app.getExpenseCategories()) {
            
            double total = this.app.getUser().getTotalByCategory(expCateg, Expense.class.getSimpleName());
            if (total != 0) {
            
                Label lb = new Label(expCateg.getName()
                    + ":   "
                    + NumberFormat.getCurrencyInstance().format(total)
                    + ""
                );
                
                Rectangle rect = new Rectangle();
                
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.getChildren().add(lb);
                anchorPane.getChildren().add(rect);
                
                lb.setLayoutX(14);
                lb.setLayoutY(14);
                lb.setTextFill(expCateg.getColor().darker().darker());
                lb.setFont(new Font(13));
                
                rect.setLayoutX(14);
                rect.setLayoutY(34);
                rect.setHeight(20.0);
                
                // Perfumaria ////////
                rect.setArcHeight(5.0);
                rect.setArcWidth(5.0);
                DropShadow ds = new DropShadow();
                ds.setOffsetY(5.0);
                ds.setOffsetX(5.0);
                ds.setColor(Color.GRAY);
                rect.setEffect(ds);
                rect.setFill(expCateg.getColor());
                //////////////////////
                
                rect.setWidth(total*1000/max);
                
                vBox.getChildren().add(anchorPane);
            }
        }
        vBox.getChildren().add(new Label()); //Espaço vazio
    }
        
    private void showIncomesByCategory(Calendar initialDate, Calendar finalDate) {
        
        double max = 2500;//temporario
        
        VBox vBox = new VBox(8);
        this.incScrollPane.setContent(vBox);
        
        for (IncomeCategory incCateg : this.app.getIncomeCategories()) {
            
            double total = this.app.getUser().getTotalByCategory(incCateg, Income.class.getSimpleName());
            if (total != 0) {
                
                Label lb = new Label(incCateg.getName()
                    + ":   "
                    + NumberFormat.getCurrencyInstance().format(total)
                    + ""
                );
                
                Rectangle rect = new Rectangle();
                
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.getChildren().add(lb);
                anchorPane.getChildren().add(rect);
                
                lb.setLayoutX(14);
                lb.setLayoutY(14);
                lb.setTextFill(incCateg.getColor().darker().darker());
                lb.setFont(new Font(13));
                
                rect.setLayoutX(14);
                rect.setLayoutY(34);
                rect.setHeight(20.0);
                
                // Perfumaria ////////
                rect.setArcHeight(5.0);
                rect.setArcWidth(5.0);
                DropShadow ds = new DropShadow();
                ds.setOffsetY(5.0);
                ds.setOffsetX(5.0);
                ds.setColor(Color.GRAY);
                rect.setEffect(ds);
                rect.setFill(incCateg.getColor());
                //////////////////////
                
                rect.setWidth(total*1000/max);
                
                vBox.getChildren().add(anchorPane);
            }
        }    
        vBox.getChildren().add(new Label()); //Espaço vazio
    }
    
    private void showForm() {
        this.app.getUser().deleteObserver(this.accountsController); 
        this.borderPane.setLeft(this.form);      
    }
    
    @FXML
    private void onAccountsGestor() {          
        this.app.showAccountsGestor();    
    }
    
    @FXML
    private void onOpen() {          
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione o arquivo...");
        FileChooser.ExtensionFilter extFilter =
            new FileChooser.ExtensionFilter("Arquivo JSON", "*.json", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(this.app.getWindow());
        if (file != null) {
            this.app.open(file.getAbsoluteFile().toString());
        }
    }
    
    @FXML
    private void onSave() {          
        this.app.save();
    }
    
    @FXML
    private void onSaveAs() {          
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar arquivo...");
        FileChooser.ExtensionFilter extFilter =
            new FileChooser.ExtensionFilter("Arquivo JSON", "*.json", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(this.app.getDefaultFileName());
        File file = fileChooser.showSaveDialog(this.app.getWindow());
        if (file != null) {        
            this.app.save(file.getAbsoluteFile().toString()); 
        }
    }
    
    public void init(FinancesApp app) {
        this.app = app;
        this.update(null, null);
        this.accountsController.init(this.app);
        this.formController.init(this.app);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.accountFilter = "";
        this.typeFilter = "";
        this.categoryFilter = "";  
        
        this.dateColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();                  
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");              
                property.setValue(formatter.format(payment.getValue().getDate()));
                return property;      
            }
        });
        
        this.descriptionColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();                  
                property.setValue(payment.getValue().getTransaction().getDescription());
                return property;      
            }
        });
        
        this.informationColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();                  
                property.setValue(payment.getValue().getTransaction().getInformation());
                return property;      
            }
        });
        
        this.valueColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();
                NumberFormat formatter = NumberFormat.getCurrencyInstance(); 
                property.setValue(formatter.format(payment.getValue().getValue()));
                return property;      
            }
        });
        
        this.concretizedColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();
                property.setValue(payment.getValue().hasConcretized() ? "Sim" : "Não");
                return property;      
            }
        });
        
        this.accountColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();
                property.setValue(payment.getValue().getTransaction().getAccount().getName());
                return property;      
            }
        });
        
        this.typeColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();
                property.setValue(payment.getValue().getTransaction().getClass().getSimpleName().equalsIgnoreCase("Income") ? "Receita" : "Despesa");
                return property;      
            }
        });
        
        this.categoryColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();
                property.setValue(payment.getValue().getTransaction().getCategory().getName());
                return property;      
            }
        });
        
        this.tableView.setRowFactory(tv -> {
            TableRow<Payment> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && (!row.isEmpty()) ) {
                    Payment payment = row.getItem();
                    this.editTransaction(payment);
                }
            });
            return row;
        });
        
        this.transactions = FXCollections.observableArrayList();
        
        this.tableView.setItems(this.transactions);

        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getClass().getResource("/financesapp/view/Accounts.fxml")
            );
            this.accountsView = loader.load();        
            this.accountsController = loader.getController();
            this.borderPane.setLeft(this.accountsView);            
        } catch(Exception e) {
            this.accountsView = null;
            this.accountsController = null;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/financesapp/view/TransactionsForm.fxml")
            );
            this.form = loader.load();
            this.formController = loader.getController();
        } catch(Exception e) {
            this.form = null;
            this.formController = null;
        }
    }
    
    //falta o filtro de período
    public void showTransactions() {
        this.transactions.clear();
        
        for (Account account : this.app.getUser().getAccounts()) {
            if (!this.accountFilter.isEmpty() && !account.getName().equalsIgnoreCase(this.accountFilter)) {
                continue;
            }
            for (Transaction transaction : account.getTransactions()) {
                if (!this.typeFilter.isEmpty() && !transaction.getClass().getSimpleName().equalsIgnoreCase(this.typeFilter)) {
                    continue;
                }                
                if (!this.categoryFilter.isEmpty() && !transaction.getCategory().getName().equalsIgnoreCase(this.categoryFilter)) {
                    continue;
                }
                this.transactions.addAll(transaction.getPayments());
            }
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {  
        // Teste ///////////////////////////////
        Calendar first = Calendar.getInstance();
        Calendar last  = first;
        first.set(Calendar.DAY_OF_MONTH, 1);
            // 01/MES/ANO
        last .set(Calendar.DAY_OF_MONTH, last.getActualMaximum(Calendar.DAY_OF_MONTH));
            // <ultimo dia do mes>/MES/ANO
        ////////////////////////////////////////
        this.showExpensesByCategory(first, last);
        this.showIncomesByCategory(first, last);
        this.showTransactions();
    }
    
}
