package financesapp.controller;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import financesapp.*;
import static java.lang.Thread.sleep;
import financesapp.model.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class TransactionsForm implements Initializable {

    //Referências para o relatório de contas
    private Parent accountsView;
    private Accounts accountsController;

    //Referência da classe principal
    private FinancesApp app;

    //Transação para add/edit
    private Transaction transaction;
    
    //tipo de transacao
    private String tipo;
    
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
    private TextField descricao;
    
    @FXML
    private TextArea informacoes;
    
    @FXML
    private DatePicker data;
    
    @FXML
    private ToggleButton buttonStatus;
    
    public void setTransaction(Transaction transaction, String Tipo) {
        
        this.limpar();
        this.tipo = Tipo;
        this.transaction = transaction;
        
        this.label.setText(Tipo);
        this.label.setFont(new Font(24));
        
        this.error.setVisible(false);
        this.category.getItems().clear();  //deletar categorias do comboboxif(Tipo.equals("Despesa")){

        if(Tipo.equals("Despesa")){
            this.buttonStatus.setText("Pago");
            Iterator<ExpenseCategory> categorys = this.app.getExpenseCategories().iterator();
            while (categorys.hasNext()) {
                Category category = categorys.next();
                this.category.getItems().add(category.getName());
            }
        }else{
            this.buttonStatus.setText("Recebido");
            Iterator<IncomeCategory> categorys = this.app.getIncomeCategories().iterator();
            while (categorys.hasNext()) {
                Category category = categorys.next();
                this.category.getItems().add(category.getName());
            }
        }

        // limpa itens existente no comboBox Contas
        this.account.getItems().clear();
        // insere contas do usuário
        for(Account acc : this.app.getUser().getAccounts()){
            this.account.getItems().add(acc.getName());
        }

        //caso venha para editar umas transacao
        if(transaction.getCategory() != null){
            //carregar dados para editar
            this.data.setValue(transaction.getDate());  //data
            this.value.setText(String.valueOf(transaction.getPayments().get(0).getValue())); //valor
            this.category.setValue(transaction.getCategory().getName());
            this.account.setValue(transaction.getAccount().getName());
            this.descricao.setText(transaction.getDescription());  //descricao
            this.informacoes.setText(transaction.getInformation()); //informacoes
            this.buttonStatus.setSelected(transaction.getPayments().get(0).hasConcretized());
        }
    }

    @FXML
    private void onSave() {
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
    }
    
    @FXML
    private void ocultarError(){
       this.error.setVisible(false); 
    }
    
    @FXML
    private void onLimpar() {
        this.limpar();
    }
    
    @FXML
    private void onCancel() {
        this.close();
    }
    
    private void limpar(){
        this.data.setValue(null);
        this.value.setText(null);
        this.category.setValue(null);
        this.account.setValue(null);
        this.descricao.setText(null);
        this.informacoes.setText(null);
        this.buttonStatus.setSelected(false);
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
        this.transaction = null;
    }

}
