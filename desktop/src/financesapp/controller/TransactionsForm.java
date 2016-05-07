package financesapp.controller;

import financesapp.*;
import financesapp.model.*;
import java.net.URL;
import java.time.*;
import java.util.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.util.*;

public class TransactionsForm implements Initializable, Observer {

    //Referências para o relatório de contas
    private Parent accountsView;
    private Accounts accountsController;

    //Referência da classe principal
    private FinancesApp app;

    //Transação para add/edit
    private Transaction transaction;
    
    private ObservableList<Account> accountList;
    
    private ObservableList<Category> expenseCategories;
    
    private ObservableList<Category> incomeCategories;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private TextField value;
    
    @FXML
    private Label label;
    
    @FXML
    private Label error;
    
    @FXML
    private ComboBox<Category> category;
    
    @FXML
    private ComboBox<Account> account;
    
    @FXML
    private TextField description;
    
    @FXML
    private TextArea information;
    
    @FXML
    private DatePicker date;
    
    @FXML
    private ToggleButton status;
    
    public void setTransaction(Transaction transaction) {        
        this.transaction = transaction;
        
        this.clearForm();
         
        String type = "Despesa";         
        String concretized = "Pago";
            
        if (this.transaction.getClass().getSimpleName().equals(Income.class.getSimpleName())) {
            type = "Receita";
            concretized = "Recebido";
            this.category.getItems().setAll(this.incomeCategories);
        } else {
            this.category.getItems().setAll(this.expenseCategories);            
        }
        
        this.label.setText(type);
        this.status.setText(concretized); 
                
        if (this.transaction.getAccount() != null) {
            this.date.setValue(transaction.getDate());
            this.category.setValue(transaction.getCategory());
            this.account.setValue(transaction.getAccount());
            this.description.setText(transaction.getDescription());
            this.information.setText(transaction.getInformation());
   
            this.value.setText(String.valueOf(transaction.getPayments().get(0).getValue()));
            this.status.setSelected(transaction.getPayments().get(0).hasConcretized());
            
            this.account.setDisable(true);
        }
        
        this.date.requestFocus();
    }

    @FXML
    private void onSave() {        
        if (this.value.getText().isEmpty()) {
            this.error.setText("*Favor digite o valor.");
            this.error.setVisible(true); 
            return;
        }
        
        if (this.category.getValue() == null) {
            this.error.setText("*Favor selecione uma categoria.");
            this.error.setVisible(true); 
            return;
        }
        
        if (this.account.getValue() == null) {
            this.error.setText("*Favor selecione uma conta.");
            this.error.setVisible(true); 
            return;
        }
        
        if (this.date.getValue() == null) {
            this.error.setText("*Favor informe uma data.");
            this.error.setVisible(true); 
            return;
        }
        
        this.transaction.setDate(date.getValue());        
        this.transaction.setCategory(this.category.getValue());
        
        this.transaction.setDescription(this.description.getText());
        this.transaction.setInformation(this.information.getText());
        
        this.transaction.getPayments().clear();
        
        double payValue = 0;
        
        try {
            payValue = Double.parseDouble((this.value.getText()));
        } catch(Exception e) {
            
        }
        
        this.transaction.addPayments(payValue, this.transaction.getDate(), this.status.isSelected());
       
        if (this.transaction.getAccount() == null) {
            this.account.getValue().addTransaction(this.transaction);
        }

        this.app.getUser().update();
        this.close();
    }
    
    @FXML
    private void hideError() {
       this.error.setVisible(false); 
    }
    
    @FXML
    private void onClear() {
        this.clearForm();
    }
    
    @FXML
    private void onCancel() {
        this.close();
    }
    
    private void clearForm() {
        this.date.setValue(LocalDate.now());
        this.value.setText("");
        this.category.setValue(null);
        if (this.transaction.getAccount() == null) {
            this.account.setValue(null);
            this.account.setDisable(false);
        }
        this.description.setText("");
        this.information.setText("");
        this.status.setSelected(false);
        this.error.setVisible(false);
    }
    
    private void close() {
        if (this.accountsView != null && this.accountsController != null) {
            BorderPane bp = (BorderPane) this.borderPane.getParent();
            bp.setLeft(this.accountsView);
            this.accountsController.init(this.app);
        }
    }

    public void init(FinancesApp app) {
        this.app = app;
        this.app.getUser().addObserver(this);
        
        this.expenseCategories = FXCollections.observableArrayList(
            this.app.getExpenseCategories()
        );
        
        this.incomeCategories = FXCollections.observableArrayList(
            this.app.getIncomeCategories()
        );
        
        this.accountList = FXCollections.observableArrayList();
        
        this.account.setItems(this.accountList);
        
        this.update(null, null);
        
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/financesapp/view/Accounts.fxml")
            );
            this.accountsView = loader.load();
            this.accountsController = loader.getController();
        } catch (Exception e) {
            this.accountsView = null;
            this.accountsController = null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.label.setFont(new Font(24));
        
        this.category.setCellFactory(new Callback<ListView<Category>, ListCell<Category>>(){
            @Override
            public ListCell<Category> call(ListView<Category> l){
                return new ListCell<Category>(){
                    @Override
                    protected void updateItem(Category item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                } ;
            }
        });
        
        this.category.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category item) {
                if (item == null) {
                  return null;
                } else {
                  return item.getName();
                }
            }

            @Override
            public Category fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        this.account.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>(){
            @Override
            public ListCell<Account> call(ListView<Account> l){
                return new ListCell<Account>(){
                    @Override
                    protected void updateItem(Account item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                        }
                    }
                } ;
            }
        });
        
        this.account.setConverter(new StringConverter<Account>() {
            @Override
            public String toString(Account item) {
                if (item == null) {
                  return null;
                } else {
                  return item.getName();
                }
            }

            @Override
            public Account fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        this.transaction = null;
    }  

    @Override
    public void update(Observable o, Object arg) {        
        this.accountList.setAll(this.app.getUser().getAccounts());
    }

}
