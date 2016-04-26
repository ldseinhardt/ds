package financesapp;

import financesapp.interfaces.*;
import java.util.*;
import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

public class FinancesApp extends Application {
    
    //Usuário
    private User user;
    
    //Referência janela principal
    private Stage window;
    
    //Categorias de despesas
    private ArrayList<ExpenseCategory> expenseCategories;
    
    //Categorias de receitas
    private ArrayList<IncomeCategory> incomeCategories;
    
    @Override
    public void start(Stage stage) throws Exception {
        this.user = new User("Usuário test");
        this.expenseCategories = new ArrayList();
        this.incomeCategories = new ArrayList();
        this.window = stage;
        
//------Testes
        user.addAccount(new DefaultAccount("Carteira" ,   3.25));
        user.addAccount(new DefaultAccount("Caixa"    , 125.47));
        user.addAccount(new DefaultAccount("Santander", 215.05));
        user.addAccount(new DefaultAccount("Bolsa"    ,   7.40));
        user.addAccount(new DefaultAccount("Poupança" , 398.89));
//------------        

        FXMLLoader loader = new FXMLLoader(getClass().getResource("interfaces/MainView.fxml"));        
        Parent root = loader.load();
      
        MainController main = loader.getController();
        main.init(this);
        
        Scene scene = new Scene(root);
        this.window.setTitle("FinancesApp");
        this.window.setMaximized(true);
        this.window.setScene(scene);
        this.window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public User getUser() {
        return this.user;
    }
    
    public void showAccountsGestor() throws Exception {   
        Stage stage = new Stage();
        stage.initOwner(this.window);
        stage.setTitle("Gestor de Contas");
        stage.initModality(Modality.APPLICATION_MODAL);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("interfaces/AccountsGestorView.fxml"));        
        Parent root = loader.load();
        
        AccountsGestorController accountsGestor = loader.getController();
        accountsGestor.init(this);
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();     
    }
}
