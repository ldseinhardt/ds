package financesapp.interfaces;

import financesapp.*;
import java.net.URL;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class AccountsGestorFormController implements Initializable {
    
    //Referência da classe principal
    private FinancesApp app;
    
    //Referência da conta para edição
    private Account account;
    
    @FXML
    private GridPane gridPane;
    
    @FXML
    private TextField name;
    
    @FXML
    private TextField openingBalance;
    
    @FXML
    private void onSave() {
        if (!this.name.getText().equalsIgnoreCase("")) {
            double balance = 0;
            if (!this.name.getText().equalsIgnoreCase("")) {
                try {
                    balance = Double.parseDouble(
                        this.openingBalance.getText()
                    );
                } catch(Exception e) {
                    
                }
            }                    
            if (this.account == null) {
                this.app.getUser().addAccount(               
                    new DefaultAccount(this.name.getText(), balance)
                );  
            } else {
                this.account.setName(this.name.getText());
                this.account.setOpeningBalance(balance);
            }
            this.app.getUser().update();
            this.close();   
        }
    } 
    
    @FXML
    private void onCancel() {
        this.close();
    } 
    
    private void close() {
        BorderPane borderPane = (BorderPane) gridPane.getParent();
        borderPane.setBottom(null);        
    }
    
    public void init(FinancesApp app) {
        this.app = app;
    }
    
    public void setAccount(Account account) {
        this.account = account;
        
        this.name.clear();
        
        double balance = 0;
        
        if (this.account != null) {
            this.name.setText(this.account.getName());
            balance = this.account.getOpeningBalance();
        }
        
        this.openingBalance.setText(String.valueOf(balance));
        
        this.name.requestFocus();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.account = null;
    } 
    
}
