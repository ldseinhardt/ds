package financesapp.interfaces;

import financesapp.*;
import java.io.File;
import java.net.*;
import java.text.NumberFormat;
import java.text.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.util.Callback;

public class MainController implements Initializable, Observer {
    
    //Referência da classe principal
    private FinancesApp app;
    
    //Referências para o relatório de contas
    private Parent accountsView;
    private AccountsController accountsController;
    
    //Referências para o formulário
    private Parent form;
    private TransactionsFormController formController;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private ScrollPane expScrollPane;
   
    @FXML
    private ScrollPane incScrollPane;
    
    //Tabela de Transações
    private TableView<Payment> tableView;
    
    ObservableList<Payment> transactions;   
    
    @FXML
    private Tab transactionsTab;
    
    @FXML
    private void onAddExpense() { 
        if (this.form != null && this.formController != null) { 
            // exemplo (passar conta selecionada de fato)
            Account selectedAccount = this.app.getUser().getAccount(this.app.getUser().getAccounts().size()-1);
            this.formController.setAccount(selectedAccount);
            this.formController.setTransaction(new Expense());
            this.showForm();       
        }       
    }
    
    @FXML
    private void onAddIncome() {
        if (this.form != null && this.formController != null) { 
            // exemplo (passar conta selecionada de fato)
            Account selectedAccount = this.app.getUser().getAccount(this.app.getUser().getAccounts().size()-1);
            this.formController.setAccount(selectedAccount);
            this.formController.setTransaction(new Income());
            this.showForm();        
        }    
    }
    
    private void showExpensesByCategory(Calendar initialDate, Calendar finalDate) {
        
        double max = 1000;//temporario
        
        VBox vBox = new VBox(8);
        this.expScrollPane.setContent(vBox);
        
        for(ExpenseCategory expCateg : this.app.getExpenseCategories()){
            
            double total = this.app.getUser().getTotalByCategory(expCateg);
            if(total != 0){
            
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
                lb.setTextFill(Color.web(expCateg.getColor()).darker().darker());
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
                rect.setFill(Color.web(expCateg.getColor()).brighter());
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
        
        for(IncomeCategory incCateg : this.app.getIncomeCategories()){
            
            double total = this.app.getUser().getTotalByCategory(incCateg);
            if(total != 0){
                
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
                lb.setTextFill(Color.web(incCateg.getColor()).darker().darker());
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
                rect.setFill(Color.web(incCateg.getColor()).brighter());
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
     
        this.tableView = new TableView();      

        transactionsTab.setContent(this.tableView);
        
        TableColumn dateColunm = new TableColumn("Data");
        dateColunm.setMinWidth(150);
        dateColunm.setMaxWidth(150);
        dateColunm.setPrefWidth(150);
        dateColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();                  
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");              
                property.setValue(formatter.format(payment.getValue().getDate()));
                return property;      
            }
        });
        
        TableColumn descriptionColunm = new TableColumn("Descrição");
        descriptionColunm.setMinWidth(250);
        descriptionColunm.setMaxWidth(250);
        descriptionColunm.setPrefWidth(250);
        descriptionColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();                  
                property.setValue(payment.getValue().getTransaction().getDescription());
                return property;      
            }
        });
        
        TableColumn valueColunm = new TableColumn("Valor");
        valueColunm.setMinWidth(150);
        valueColunm.setMaxWidth(150);
        valueColunm.setPrefWidth(150);
        valueColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();
                NumberFormat formatter = NumberFormat.getCurrencyInstance(); 
                property.setValue(formatter.format(payment.getValue().getValue()));
                return property;      
            }
        });
        
        //add concretizado ??
        
        // add nome da conta?? 
        // colocar uma ref em Transaction da conta
        
        TableColumn categoryColunm = new TableColumn("Categoria");
        categoryColunm.setMinWidth(200);
        categoryColunm.setMaxWidth(200);
        categoryColunm.setPrefWidth(200);
        categoryColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();
                property.setValue(payment.getValue().getTransaction().getCategory().getName());
                return property;      
            }
        });
        
        this.transactions = FXCollections.observableArrayList();
        
        this.tableView.getColumns().addAll(
            dateColunm, descriptionColunm, valueColunm, categoryColunm
        );
        this.tableView.setItems(this.transactions);
        
        //add menus de contexto para adicionar despesas e receitas, editar e remover
        
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("AccountsView.fxml")
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
                getClass().getResource("TransactionsFormView.fxml")
            );
            this.form = loader.load();
            this.formController = loader.getController();
        } catch(Exception e) {
            this.form = null;
            this.formController = null;
        }
    }
    
    public void showTransactions() {
        //exemplo abaixo
        // fazer um filtro com o pagamento de transações necessárias de acordo com os filtros
        // e por os os pagamentos em transactions
        this.transactions.clear();
        
        Account a = this.app.getUser().getAccount(5);
        
        if (a != null) {
            Transaction t = a.getTransaction(0);
            
            ArrayList<Payment> pays = a.getTransaction(0).getPayments();
  
            this.transactions.addAll(a.getTransaction(0).getPayments());
            this.transactions.addAll(a.getTransaction(1).getPayments());
            
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
