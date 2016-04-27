package financesapp.interfaces;

import financesapp.*;
import java.net.URL;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.layout.*;

public class TransactionsFormController implements Initializable {
    
    //Referências para o formulário
    private Parent accountsView;
    private AccountsController accountsController;
    
    //Referência da classe principal
    private FinancesApp app;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML    
    private void onCancel() {
        if (this.accountsView != null && this.accountsController != null) {            
            BorderPane bp = (BorderPane) this.borderPane.getParent();
            bp.setLeft(this.accountsView);
            this.accountsController.init(this.app);
        }
    }
    
    public void init(FinancesApp app) {
        this.app = app;                     
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("AccountsView.fxml")
            );
            this.accountsView = loader.load();
            this.accountsController = loader.getController();
        } catch(Exception e) {
            this.accountsView = null;
            this.accountsController = null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) { 
        
    }  
    
}
