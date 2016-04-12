package financesapp;

import java.time.LocalDate;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.*;
import org.json.*;

public class FinancesApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("FinancesApp");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));        
        Parent root = loader.load();
        
        MainController main = loader.getController();
        
        stage.setOnCloseRequest(e -> main.save());
        stage.setScene(new Scene(root));
        stage.show();
        
        //Testes
        User user = new User("Usuário Padrão");
        System.out.println(user.getName());
        
        DefaultAccount carteira = new DefaultAccount("Conta Carteira");
        user.addAccount(carteira);
        carteira.addTransaction(new Recipe(75, LocalDate.now(), 1, true, "Serviço prestado", ""));
        carteira.addTransaction(new Expense(010.0, LocalDate.now(), 1, true, "Compra 1", ""));
        carteira.addTransaction(new Expense(022.5, LocalDate.now(), 1, true, "Compra 2", ""));
        carteira.addTransaction(new Expense(100.0, LocalDate.now(), 1, true, "Compra 3", ""));
        System.out.println(carteira.getName());
        System.out.println(carteira.getBalanceTotal());
        
        
        DefaultAccount caixa = new DefaultAccount("Conta Caixa", 3750.76);
        user.addAccount(caixa);
        System.out.println(caixa.getName());
        System.out.println(caixa.getBalanceTotal());
        
        System.out.println(user.getBalanceTotal());
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
