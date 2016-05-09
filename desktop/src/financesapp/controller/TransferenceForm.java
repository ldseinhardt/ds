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

public class TransferenceForm implements Initializable, Observer {

    //Referências para o relatório de contas
    private Parent accountsView;
    private Accounts accountsController;

    //Referência da classe principal
    private FinancesApp app;

    //Transações para add
    private TransferenceIn transferenceIn;
    private TransferenceOut transferenceOut;

    //Pagamentos para edit
    private Payment paymentIn;
    private Payment paymentOut;
    
    private ObservableList<Account> accountList;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private TextField value;
    
    @FXML
    private Label transactionType;
    
    @FXML
    private Label error;
    
    @FXML
    private ComboBox<Account> fromAccount;
    
    @FXML
    private ComboBox<Account> toAccount;
    
    @FXML
    private TextArea information;
    
    @FXML
    private DatePicker date;
    
    @FXML
    private Button save;
    
    public void setTransference(
        TransferenceIn transferenceIn,
        TransferenceOut transferenceOut) {
        
        this.transferenceIn = transferenceIn;
        this.transferenceOut = transferenceOut;
        this.paymentIn = null;
        this.paymentOut = null;
        
        this.clearForm();
        
        String type = "Transferência";
        
        this.value.getStyleClass().add("transference-text-color");
        this.save.getStyleClass().add("transference-button-color");
        
        this.transactionType.setText(type);
        
    }
    
    public void setPayment(Payment paymentIn, Payment paymentOut) {
        /*
        this.transferenceIn = null;
        this.transferenceOut = null;
        this.paymentIn = paymentIn;
        this.paymentOut = paymentOut;
        
        this.clearForm();
         
        String type = "Transferência";
            
        this.value.getStyleClass().add("transference-text-color"); 
        this.save.getStyleClass().add("transference-button-color");              
        
        this.transactionType.setText(type);
        
        this.date.setValue(this.paymentIn.getDate());
        this.fromAccount.setValue(this.paymentOut.getTransaction().getAccount());
        this.toAccount.setValue(this.paymentIn.getTransaction().getAccount());
        this.information.setText(this.paymentIn.getTransaction().getInformation());
        
        this.value.setText(String.valueOf(this.paymentIn.getValue()));
        /*
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
        */
        this.date.requestFocus();
    }

    @FXML
    private void onSave() {
        /*
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
        
        if (this.fromAccount.getValue() == null) {
            this.error.setText("*Favor selecione a conta de origem.");
            this.error.setVisible(true); 
            return;
        }
        
        if (this.toAccount.getValue() == null) {
            this.error.setText("*Favor selecione a conta de destino.");
            this.error.setVisible(true); 
            return;
        }
        
        if (this.date.getValue() == null) {
            this.error.setText("*Favor informe uma data.");
            this.error.setVisible(true); 
            return;
        }
        
        TransferenceIn transIn = null;
        TransferenceOut transOut = null;
                
        LocalDate today = LocalDate.now();      
        
        if (this.transferenceIn != null) {
            //add
            transIn = this.transferenceIn;
            
            transIn.setDate(date.getValue());  
            
            transferenceIn.addPayment(new Payment(
                transValue, this.date.getValue(), true
            ));
        
        } else {
            if (this.paymentIn != null) {
                //edit
                transIn = this.paymentIn.getTransaction();
                this.paymentIn.setDate(date.getValue());
                this.paymentIn.setValue(transValue);
            }
            if (this.paymentOut != null) {
                //edit
                transOut = this.paymentOut.getTransaction();
                this.paymentOut.setDate(date.getValue());
                this.paymentOut.setValue(transValue);
            }
        }
        
        if (trans != null) {
            trans.setInformation(this.information.getText());
            
            if (trans.getAccount() == null) {
                this.fromAccount.getValue().addTransaction(trans);
                this.toAccount.getValue().addTransaction(trans);
            } else if(trans.getAccount() != this.fromAccount.getValue()) {
                trans.getAccount().getTransactions().remove(trans);
                this.fromAccount.getValue().addTransaction(trans);
            }
        }
        */
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
        //this.value.setText(String.valueOf(0.0));
        this.value.setText("");
        this.fromAccount.setValue(null);
        this.toAccount.setValue(null);
        this.information.setText("");
        this.error.setVisible(false);
        this.date.requestFocus();
        this.value.getStyleClass().removeAll(
                "transference-text-color",
                "transference-button-color"
        );
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
        
        this.accountList = FXCollections.observableArrayList();
        
        this.fromAccount.setItems(this.accountList);
        this.toAccount.setItems(this.accountList);
        
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
        
        this.fromAccount.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
            @Override
            public ListCell<Account> call(ListView<Account> account) {
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
        
        this.toAccount.setCellFactory(new Callback<ListView<Account>, ListCell<Account>>() {
            @Override
            public ListCell<Account> call(ListView<Account> account) {
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
        
        this.fromAccount.setConverter(new StringConverter<Account>() {
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
        
        this.toAccount.setConverter(new StringConverter<Account>() {
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
        
        //this.transaction = null;
        //this.payment = null;
    }  

    @Override
    public void update(Observable o, Object arg) {        
        this.accountList.setAll(this.app.getUser().getAccounts());
        //Previne que o usuário adicione em uma conta removida
        if (this.fromAccount.getValue() != null && 
            !this.app.getUser().getAccounts().contains(this.fromAccount.getValue())) {            
            this.fromAccount.setValue(null);
        }
        if (this.toAccount.getValue() != null && 
            !this.app.getUser().getAccounts().contains(this.toAccount.getValue())) {            
            this.toAccount.setValue(null);
        }
    }

}
