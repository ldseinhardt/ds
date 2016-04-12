package financesapp;

import java.util.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;

public class FinancesApp extends Application {
    
    //Usuário
    private User user;
    
    //Categorias de despesas
    private ArrayList<ExpenseCategory> expenseCategories;
    
    //Categorias de receitas
    private ArrayList<RecipeCategory> recipeCategories;

    @Override
    public void start(Stage stage) throws Exception {
        this.user = new User();
        this.expenseCategories = new ArrayList();
        this.recipeCategories = new ArrayList();
        
        stage.setTitle("FinancesApp");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));        
        Parent root = loader.load();
        
        MainController main = loader.getController();
        main.setUser(this.user);
        main.setExpenseCategories(this.expenseCategories);
        main.setRecipeCategories(this.recipeCategories);
        main.load();
        
        stage.setOnCloseRequest(e -> main.save());
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {        
        launch(args);
    }
    
}
