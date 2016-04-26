package financesapp.interfaces;

import financesapp.*;
import java.net.URL;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class AccountsGestorAddController implements Initializable, Observer {
    
    //Referência da classe principal
    private FinancesApp app;
    
    @FXML
    private GridPane gridPane;
    
    @FXML
    private TextField name;
    
    @FXML
    private TextField openingBalance;
    
    @FXML
    private void onSave() {
        this.app.getUser().addAccount(new DefaultAccount(name.getText(), 0));
        this.close();
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
        this.update(null, null);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        name.clear();
        openingBalance.clear();
    }  

    @Override
    public void update(Observable o, Object arg) {        
        
    }
}
