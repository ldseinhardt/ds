package financesapp.interfaces;

import financesapp.*;
import java.net.URL;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ExpByCategController implements Initializable, Observer {
    
    //Referência da classe principal
    private FinancesApp app;
    
    @FXML
    private ScrollPane sp;

    private void showReport() {
        VBox vBox = new VBox(5);
        this.sp.setContent(vBox);
        /*
        for(ExpenseCategory expCateg : this.app.getExpenseCategories()){
            
            Label lb = new Label(expCateg.getName());
            
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.getChildren().add(lb);
            vBox.getChildren().add(anchorPane);
            
            lb.setLayoutX(14);
            lb.setLayoutY(14);
        }
        */
        for(int i=1;i<11;i++){
            Label lb = new Label("Categoria " + i);
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.getChildren().add(lb);
            vBox.getChildren().add(anchorPane);
            lb.setLayoutX(14);
            lb.setLayoutY(14);
        }
    }
    
    public void init(FinancesApp app) {
        this.app = app;
        this.app.getUser().addObserver(this);
        this.update(null, null);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        this.showReport();
    }  

    @Override
    public void update(Observable o, Object arg) {        
        this.showReport();
    }
    
}
