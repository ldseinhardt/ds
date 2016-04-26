package financesapp;

import financesapp.interfaces.*;
import java.io.*;
import java.util.*;
import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

public class FinancesApp extends Application {
    
    //Usuário
    private User user;
    
    //Nome padrão do arquivo
    private final String data_filename = "data_user.json"; 
    
    //Referência janela principal
    private Stage window;
    
    //Categorias de despesas
    private ArrayList<ExpenseCategory> expenseCategories;
    
    //Categorias de receitas
    private ArrayList<IncomeCategory> incomeCategories;
    
    @Override
    public void start(Stage stage) throws Exception {
        this.user = new User();
        this.expenseCategories = new ArrayList();
        this.incomeCategories = new ArrayList();
        this.window = stage;
        
        this.user.loadFromJSONString(this.loadFile(this.data_filename));
        
        /* Testes
        user.addAccount(new DefaultAccount("Carteira" ,   3.25));
        user.addAccount(new DefaultAccount("Caixa"    , 125.47));
        user.addAccount(new DefaultAccount("Santander", 215.05));
        user.addAccount(new DefaultAccount("Bolsa"    ,   7.40));
        user.addAccount(new DefaultAccount("Poupança" , 398.89));
        */
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("interfaces/MainView.fxml"));        
        Parent root = loader.load();
      
        MainController main = loader.getController();
        main.init(this);
        
        Scene scene = new Scene(root);
        this.window.setTitle("FinancesApp");
        this.window.setMaximized(true);
        this.window.setScene(scene);
        this.window.show();
        this.window.setOnCloseRequest(e -> this.save());
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
    
    public void showAccountsGestor() {   
        try {
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
        } catch(Exception e) {
            
        } 
    }
    
    public void save() {
        this.saveFile(this.data_filename, this.user.toJSONString());
    }
    
    private void saveFile(String filename, String content) {
        try {
            File file = new File(filename);
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);   
            bw.write(content);  
            bw.flush();    
        } catch(Exception e) {
            
        }
    }
    
    private String loadFile(String filename) {
        try {
            File file = new File(filename);
            FileReader fr = new FileReader(file); 
            BufferedReader br = new BufferedReader(fr); 
            String line, content = ""; 
            while((line = br.readLine()) != null){
                content += line;
            }
            return content;
        } catch(Exception e) {
            
        }
        return null;        
    }
}
