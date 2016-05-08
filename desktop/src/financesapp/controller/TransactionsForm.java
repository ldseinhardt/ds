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
import javafx.util.*;

public class TransactionsForm implements Initializable, Observer {

    //Referências para o relatório de contas
    private Parent accountsView;
    private Accounts accountsController;

    //Referência da classe principal
    private FinancesApp app;

    //Transação para add
    private Transaction transaction;

    //Pagamento para edit    
    private Payment payment;
    
    private ObservableList<Account> accountList;
    
    private ObservableList<Category> expenseCategories;
    
    private ObservableList<Category> incomeCategories;
    
    private ObservableList<String> periods;
        
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private TextField value;
    
    @FXML
    private Label transactionType;
    
    @FXML
    private Label error;
    
    @FXML
    private ComboBox freq;
    
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
    private CheckBox status;
    
    public void setTransaction(Transaction transaction) {        
        this.transaction = transaction;
        this.payment = null;
        
        this.clearForm();
         
        String type = "Despesa";         
        String concretized = "Pago";
        
        this.value.getStyleClass().removeAll("expense-text", "income-text");
            
        if (this.transaction instanceof Income) {
            type = "Receita";
            concretized = "Recebido";
            this.category.getItems().setAll(this.incomeCategories);
            this.value.getStyleClass().add("income-text");
        } else {
            this.category.getItems().setAll(this.expenseCategories);  
            this.value.getStyleClass().add("expense-text");          
        }
        
        this.transactionType.setText(type);
        this.status.setText(concretized); 
        
    }
    
    public void setPayment(Payment payment) {  
        this.transaction = null;
        this.payment = payment;
        
        this.freq.setDisable(true);
        
        this.clearForm();
         
        String type = "Despesa";         
        String concretized = "Pago";
            
        if (this.payment.getTransaction() instanceof Income) {
            type = "Receita";
            concretized = "Recebido";
            this.category.getItems().setAll(this.incomeCategories);
        } else {
            this.category.getItems().setAll(this.expenseCategories);            
        }
        
        this.transactionType.setText(type);
        this.status.setText(concretized); 
        
        this.date.setValue(this.payment.getDate());
        this.category.setValue(this.payment.getTransaction().getCategory());
        this.account.setValue(this.payment.getTransaction().getAccount());
        this.description.setText(this.payment.getTransaction().getDescription());
        this.information.setText(this.payment.getTransaction().getInformation());
   
        this.value.setText(String.valueOf(this.payment.getValue()));
        this.status.setSelected(this.payment.hasConcretized());
                        
        ArrayList<Payment> pays = this.payment.getTransaction().getPayments();
        if (pays.size() > 1) {
            LocalDate d1 = pays.get(0).getDate();
            LocalDate d2 = pays.get(1).getDate();
            if (d1.plusDays(1).equals(d2)) {
                this.freq.setValue(this.periods.get(4));                
            } else if (d1.plusWeeks(1).equals(d2)) {
                this.freq.setValue(this.periods.get(3));
            } else if (d1.plusWeeks(2).equals(d2)) {
                this.freq.setValue(this.periods.get(2));
            } else if (d1.plusMonths(1).equals(d2)) {
                this.freq.setValue(this.periods.get(1));
            }
        }
      
        this.date.requestFocus();
    }

    @FXML
    private void onSave() {        
        if (this.description.getText().isEmpty()) {
            this.error.setText("*Favor digite uma descrição.");
            this.error.setVisible(true); 
            return;
        }    
        
        if (this.value.getText().isEmpty()) {
            this.error.setText("*Favor digite o valor.");
            this.error.setVisible(true); 
            return;
        }
        
        double transValue = 0;
        
        try {
            transValue = Double.parseDouble((this.value.getText()));
            if (transValue <= 0) {
                this.error.setText("*Favor digite um valor válido.");
                this.error.setVisible(true); 
                return;                   
            }
        } catch(Exception e) {
            this.error.setText("*Favor digite um valor válido.");
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
        
        Transaction trans = null;  
                
        LocalDate today = LocalDate.now();      
        
        if (this.transaction != null) {
            //add
            trans = this.transaction;
            
            trans.setDate(date.getValue());  
            
            if (this.freq.getValue() == this.periods.get(0)) {
                trans.addPayment(new Payment(
                    transValue, this.date.getValue(), this.status.isSelected()
                ));                
            } else {
                int period = 30; 
                
                if (this.freq.getValue() == this.periods.get(1)) {
                    period = 30;
                } else if (this.freq.getValue() == this.periods.get(2)) {              
                    period = 15;
                } else if (this.freq.getValue() == this.periods.get(3)) {              
                    period = 7;
                } else if (this.freq.getValue() == this.periods.get(4)) {              
                    period = 1;
                }
                
                for (LocalDate date : this.getDates(this.date.getValue(), period)) {
                    trans.addPayment(new Payment(
                        transValue, date, date.isBefore(today) || date.isEqual(today)
                    ));                    
                }                
            }
            
        } else if (this.payment != null) {
            //edit
            trans = this.payment.getTransaction();
            this.payment.setDate(date.getValue());
            this.payment.setValue(transValue);
            this.payment.setConcretized(this.status.isSelected());
        }
        
        if (trans != null) {      
            trans.setCategory(this.category.getValue());
            trans.setDescription(this.description.getText());
            trans.setInformation(this.information.getText()); 
            
            if (trans.getAccount() == null) {
                this.account.getValue().addTransaction(trans);
            } else if(trans.getAccount() != this.account.getValue()) {
                trans.getAccount().getTransactions().remove(trans);
                this.account.getValue().addTransaction(trans);
            }
        }

        this.app.getUser().update();
        this.close();
    }
    
    private ArrayList<LocalDate> getDates(LocalDate date, int period) {        
        ArrayList<LocalDate> dates = new ArrayList();
        
        int MAX_YEAR = date.getYear() + 5;              
        
        while(date.getYear() <= MAX_YEAR) {
            dates.add(date); 
            switch (period) {
                case 30:
                    date = date.plusMonths(1);
                    break;
                case 15:
                    date = date.plusWeeks(2);
                    break;
                case 7:
                    date = date.plusWeeks(1);                        
                    break;
                case 1:
                    date = date.plusDays(1);                        
                    break;
            }               
        }
        
        return dates;
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
        //this.value.setText(String.valueOf(0.0));
        this.freq.setValue(this.periods.get(0));
        this.category.setValue(null);
        this.account.setValue(null);
        this.description.setText("");
        this.information.setText("");
        this.status.setSelected(true);
        this.error.setVisible(false);
        this.date.requestFocus();
        if (this.payment == null) {
            this.freq.setDisable(false);
        }
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
        this.periods = FXCollections.observableArrayList(
            "Única",
            "Mensal",
            "Quinzenal",
            "Semanal",
            "Diária"
        );
        
        this.freq.setItems(this.periods);
        
        this.category.setCellFactory(new Callback<ListView<Category>, ListCell<Category>>() {
            @Override
            public ListCell<Category> call(ListView<Category> category){
                return new ListCell<Category>(){
                    @Override
                    protected void updateItem(Category category, boolean empty) {
                        super.updateItem(category, empty);
                        if (category == null || empty) {
                            this.setGraphic(null);
                        } else {
                            this.setText(category.getName());
                        }
                    }
                };
            }
        });
        
        this.category.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                if (category != null) {
                  return category.getName();
                }
                return null;
            }

            @Override
            public Category fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        this.account.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
            @Override
            public ListCell<Account> call(ListView<Account> account){
                return new ListCell<Account>(){
                    @Override
                    protected void updateItem(Account account, boolean empty) {
                        super.updateItem(account, empty);
                        if (account == null || empty) {
                            this.setGraphic(null);
                        } else {
                            this.setText(account.getName());
                        }
                    }
                };
            }
        });
        
        this.account.setConverter(new StringConverter<Account>() {
            @Override
            public String toString(Account account) {
                if (account != null) {
                  return account.getName();
                }
                return null;
            }

            @Override
            public Account fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        
        this.transaction = null;
        this.payment = null;
    }  

    @Override
    public void update(Observable o, Object arg) {        
        this.accountList.setAll(this.app.getUser().getAccounts());
    }

}
