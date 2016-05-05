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
    
    //Nomes das categorias de despesa
    private String[] expCategName;
    
    //Nomes das categorias de receita
    private String[] incCategName;
    
    @Override
    public void start(Stage stage) throws Exception {
        this.window = stage;
        
        // Lista de categorias para ambos tipos de transações
        this.expenseCategories = new ArrayList();
        this.incomeCategories = new ArrayList();
        
        this.expCategName = new String[]{
            "Alimentação", "Beleza", "Dívida", "Educação", "Eletricidade",
            "Higiene", "Internet", "Investimento", "Item Pessoal", "Justiça",
            "Lazer", "Limpeza", "Moradia", "Pet", "Presente", "Saneamento",
            "Saúde", "Telefonia", "Terceiros", "Transporte", "TV a Cabo",
            "Vestuário"
        };
        
        this.incCategName = new String[]{
            "Empréstimo", "Justiça", "Presente",
            "Renda", "Salário", "Serviço",
            "Terceiros"
        };
        
        for (String name : expCategName) {
            this.expenseCategories.add(new ExpenseCategory(name));
        }
        
        for (String name : incCategName) {
            this.incomeCategories.add(new IncomeCategory(name));
        }
        
        this.user = new User();
        
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("interfaces/MainView.fxml")
        );        
        Parent root = loader.load();
      
        MainController main = loader.getController();
        main.init(this);
        
        this.user.addObserver((Observer) main);
        
        // Dados padrões para um usuário novo
        this.user.setName("Usuário Padrão");
        this.user.addAccount(new DefaultAccount("Carteira"));
        this.user.update();
        
        // Carrega os dados do usuário se possível
        this.open();
        
        Scene scene = new Scene(root);
        this.window.setTitle("FinancesApp");
        this.window.setMaximized(true);
        this.window.setScene(scene);
        this.window.show();
        this.window.setOnCloseRequest(e -> this.save());
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    public void showAccountsGestor() {   
        try {
            Stage stage = new Stage();
            stage.initOwner(this.window);
            stage.setTitle("Gestor de Contas");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);

            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("interfaces/AccountsGestorView.fxml")
            );        
            Parent root = loader.load();

            AccountsGestorController accountsGestor = loader.getController();
            accountsGestor.init(this);            
            
            stage.setOnCloseRequest(e ->
                this.user.deleteObserver(accountsGestor)
            );
            
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();    
        } catch(Exception e) {
            
        } 
    }
    
    public User getUser() {
        return this.user;
    }
        
    public Stage getWindow() {
        return this.window;
    }
    
    public ArrayList<ExpenseCategory> getExpenseCategories(){
        return this.expenseCategories;
    }
    
    public ArrayList<IncomeCategory> getIncomeCategories(){
        return this.incomeCategories;
    }
    
    public void open(String filename) {
        String data = this.loadFile(filename);
        if (data != null) {
            this.user.loadFromJSONString(data);
        }    
    }
    
    public void open() {
        this.open(this.data_filename);
    }
    
    public void save(String filename) {
        this.saveFile(filename, this.user.toJSONString());
    }
    
    public void save() {
        this.save(this.data_filename);
    }
    
    public String getDefaultFileName() {
        return this.data_filename;
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
