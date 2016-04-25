package financesapp.interfaces;

import financesapp.Account;
import financesapp.DefaultAccount;
import financesapp.User;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

public class AccountsController implements Initializable {
    
    private User user;
    
    @FXML
    private AnchorPane accounts;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        this.user = new User();
//------Testes
        user.addAccount(new DefaultAccount("Carteira" ,   3.25));
        user.addAccount(new DefaultAccount("Caixa"    , 125.47));
        user.addAccount(new DefaultAccount("Santander", 215.05));
        user.addAccount(new DefaultAccount("Bolsa"    ,   7.40));
        user.addAccount(new DefaultAccount("Poupança" , 398.89));
//------------
        Iterator<Account> ac = this.user.getAccounts().iterator();
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
            accPb     .setLayoutY (61 + y);
            
            AnchorPane.setLeftAnchor  (accName   , 14.0);
            AnchorPane.setRightAnchor (accBalance, 14.0);
            AnchorPane.setLeftAnchor  (accPb     , 14.0);
            AnchorPane.setRightAnchor (accPb     , 14.0);
            y += 70;
        }
    }
    
}
