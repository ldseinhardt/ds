package financesapp;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class MainController implements Initializable {
    
    private User user;
    
    @FXML
    private List<Label> labelList;
    
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
        Iterator<Label> lb = this.labelList.iterator();
        while (ac.hasNext()) {
            Account account = ac.next();
            Label label = lb.next();
            label.setText(account.getName());
            label.setVisible(true);
        }
    }
    
}
