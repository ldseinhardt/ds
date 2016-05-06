package financesapp;

import financesapp.model.*;
import financesapp.controller.*;
import java.io.*;
import java.util.*;
import javafx.application.Application;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class FinancesApp extends Application {
    
    //Usuário
    private User user;
    
    //Nome padrão do arquivo
    private final String data_filename = "data_user.json"; 
    
    //Padrão
    private final String default_user = "Usuário Padrão"; 
    private final String default_account = "Carteira"; 
    
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
    
    private Color[] colors;
    
    @Override
    public void start(Stage stage) throws Exception {
        this.window = stage;
        
        window.getIcons()
                .add(new Image(getClass()
                .getResourceAsStream("icon.jpg")
        ));
        
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
        
        this.colors = new Color[]{
            Color.CORAL, Color.FUCHSIA, Color.RED, Color.BLUEVIOLET,
            Color.YELLOW, Color.BISQUE, Color.CADETBLUE, Color.CRIMSON,
            Color.LIGHTGRAY, Color.KHAKI, Color.LIGHTGREEN, Color.LAVENDER,
            Color.ROYALBLUE, Color.BROWN, Color.PINK, Color.BLUE, Color.GREEN,
            Color.GOLD, Color.ORANGE, Color.DARKSLATEGRAY, Color.TOMATO,
            Color.DODGERBLUE
        };
        
        int c = 0;
        for (String name : expCategName) {
            this.expenseCategories.add(new ExpenseCategory(name));
            this.expenseCategories.get(this.expenseCategories.size()-1)
                .setColor(colors[c++]);
        }
        
        for (String name : incCategName) {
            this.incomeCategories.add(new IncomeCategory(name));
            this.incomeCategories.get(this.incomeCategories.size()-1)
                .setColor(colors[--c]);
        }
        
        this.user = new User();
        FXMLLoader loader = new FXMLLoader(
            this.getClass().getResource("/financesapp/view/Main.fxml")
        );      
        Parent root = loader.load();
      
        Main main = loader.getController();
        main.init(this);
        
        this.user.addObserver((Observer) main);
        
        // Dados padrões para um usuário novo
        this.user.setName(this.default_user);
        this.user.addAccount(new DefaultAccount(this.default_account));
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
                getClass().getResource("/financesapp/view/AccountsGestor.fxml")
            );        
            Parent root = loader.load();

            AccountsGestor accountsGestor = loader.getController();
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
    
    public String getDefaultAccountName() {
        return this.default_account;
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
