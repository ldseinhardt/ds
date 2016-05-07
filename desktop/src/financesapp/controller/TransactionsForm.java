package financesapp.controller;

import financesapp.*;
import financesapp.model.*;
import java.net.URL;
import java.time.*;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class TransactionsForm implements Initializable {

    //Referências para o relatório de contas
    private Parent accountsView;
    private Accounts accountsController;

    //Referência da classe principal
    private FinancesApp app;

    //Transação para add/edit
    private Transaction transaction;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private TextField value;
    
    @FXML
    private Label label;
    
    @FXML
    private Label error;
    
    @FXML
    private ComboBox category;
    
    @FXML
    private ComboBox account;
    
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
            for (IncomeCategory category : this.app.getIncomeCategories()) {
                this.category.getItems().add(category.getName());                
            } 
        } else {  
            for (ExpenseCategory category : this.app.getExpenseCategories()) {
                this.category.getItems().add(category.getName());
            }         
        }
        
        this.label.setText(type);
        this.status.setText(concretized); 
        
        for (Account account : this.app.getUser().getAccounts()){
            this.account.getItems().add(account.getName());
        }  
        
        if (this.transaction.getAccount() != null) {
            this.date.setValue(transaction.getDate());
            this.category.setValue(transaction.getCategory().getName());
            this.account.setValue(transaction.getAccount().getName());
            this.description.setText(transaction.getDescription());
            this.information.setText(transaction.getInformation());
   
            this.value.setText(String.valueOf(transaction.getPayments().get(0).getValue()));
            this.status.setSelected(transaction.getPayments().get(0).hasConcretized());
        }
    }

    @FXML
    private void onSave() {
        /*
        if (this.transaction != null) {
            if(this.value.getText() != null){ //valor
                if(this.category.getValue() != null){   //categoria selecionada
                    if (this.account.getValue() != null) {  //conta selecionada

                        //data
                        if(data.getValue() != null){
                            this.transaction.setDate(data.getValue());  //data
                        }else{
                            this.transaction.setDate(LocalDate.now());  //data atual
                        }

                        this.transaction.setDescription(descricao.getText());  //descricao
                        this.transaction.setInformation(informacoes.getText()); //infomacao

                        //buscar categoria selecionada
                        if(tipo.equals("Despesa")){ //despesa
                            Iterator<ExpenseCategory> categorys = this.app.getExpenseCategories().iterator();
                            while (categorys.hasNext()) {
                                Category category = categorys.next();
                                if(category.getName().equals(this.category.getValue())){
                                    this.transaction.setCategory(category);
                                }
                            }
                        }else{ //receita
                            Iterator<IncomeCategory> categorys = this.app.getIncomeCategories().iterator();
                            while (categorys.hasNext()) {
                                Category category = categorys.next();
                                if(category.getName().equals(this.category.getValue())){
                                    this.transaction.setCategory(category);
                                }
                            }
                        }

                        this.transaction.addPayments(Float.parseFloat(value.getText()), transaction.getDate(), buttonStatus.isSelected());

                        Iterator<Account> accounts = this.app.getUser().getAccounts().iterator();
                        while (accounts.hasNext()) {
                            Account account = accounts.next();
                            if(account.getName().equals(this.account.getValue())){
                                this.transaction.setAccount(account);
                                account.addTransaction(this.transaction);
                            }
                        }

                        this.app.getUser().update();
                        this.close();
                    } else {
                        this.error.setText("*Favor Selecione uma Conta.");
                        this.error.setVisible(true);
                    }
                }else {
                    this.error.setText("*Favor Selecione uma Categoria.");
                    this.error.setVisible(true);
                }
            }else {
                this.error.setText("*Favor Digite o Valor da " + tipo);
                this.error.setVisible(true);
            }
        }
*/
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
        this.date.setValue(null);
        this.value.setText("");
        this.category.setValue("");
        this.account.setValue(null);
        this.description.setText("");
        this.information.setText("");
        this.status.setSelected(false);
        this.account.getItems().clear();
        this.category.getItems().clear();
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
        
        this.transaction = null;
    }

}
