package financesapp;

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
        
        //test
        User u = new User("Usuário padrão");
        System.out.println(u.getName());
        
        Account a = new DefaultAccount("Conta carteira");
        u.addAccount(a);
        System.out.println(a.getName());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
