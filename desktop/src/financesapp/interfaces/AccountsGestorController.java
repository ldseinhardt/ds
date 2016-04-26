package financesapp.interfaces;

import financesapp.*;
import java.net.URL;
import java.util.*;
import javafx.fxml.*;

public class AccountsGestorController implements Initializable {
    
    //Referência da classe principal
    private FinancesApp app;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    
    
    public void init(FinancesApp app) {
        this.app = app;
        System.out.println(this.app.getUser().getName());
    }
}
