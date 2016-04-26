package financesapp.interfaces;

import financesapp.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MainController implements Initializable, Observer {
    
    //Referência da classe principal
    private FinancesApp app;
    
    @FXML
    private AnchorPane accounts;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
    }
    
    public void init(FinancesApp app) {
        this.app = app;
        
        this.showAccounts();                     
    }
    
    public void showAccounts() {
        accounts.getChildren().clear();
        
        Iterator<Account> ac = this.app.getUser().getAccounts().iterator();
        int y = 0;
        while (ac.hasNext()) {
            
            Account account = ac.next();
            
            Label accName     = new Label(account.getName());
            Label accBalance  = new Label(
                                NumberFormat.
                                getCurrencyInstance().
                                format(account.getBalance()));
            ProgressBar accPb = new ProgressBar(account.getBalance()/500);
            //R$ 500,00: valor teste. Depois, 100% sera o maior saldo no mes.
            
            accounts.getChildren().add(accName);
            accounts.getChildren().add(accBalance);
            accounts.getChildren().add(accPb);
            
            accName   .setLayoutX (14);
            accBalance.setLayoutX (200);
            accPb     .setLayoutX (14);
            accPb     .setPrefWidth(200);
            
            accName   .setLayoutY (44 + y);
            accBalance.setLayoutY (44 + y);
            accPb     .setLayoutY (65 + y);
            
            AnchorPane.setLeftAnchor  (accName   , 14.0);
            AnchorPane.setRightAnchor (accBalance, 14.0);
            AnchorPane.setLeftAnchor  (accPb     , 14.0);
            AnchorPane.setRightAnchor (accPb     , 14.0);
            y += 70;
        } 
    }
    
    public void onAccountsGestor() {          
        this.app.showAccountsGestor();    
    }

    @Override
    public void update(Observable o, Object arg) {
        this.showAccounts();   
    }
}
