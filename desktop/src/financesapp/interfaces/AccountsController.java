package financesapp.interfaces;

import financesapp.*;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;
import javafx.fxml.*;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class AccountsController implements Initializable, Observer {
    
    //Referência da classe principal
    private FinancesApp app;
    
    @FXML
    private ScrollPane scrollPane;

    private void showAccounts() {
        VBox vBox = new VBox(5);
        this.scrollPane.setContent(vBox);
        
        double bal, prog, generalMaxBalance;
        Color balColor;
        NodeOrientation nodeOr;
        ColorAdjust colorAdj;
        
        generalMaxBalance = this.app.getUser().getGeneralMaxBalance();
        Iterator<Account> ac = this.app.getUser().getAccounts().iterator();
        while (ac.hasNext()) {
            
            Account account = ac.next();
            
            bal      = account.getBalance();
            balColor = Color.DARKGREEN;
            prog     = bal/(generalMaxBalance*1.05);
            nodeOr   = NodeOrientation.LEFT_TO_RIGHT;
            colorAdj = new ColorAdjust();
            colorAdj.setHue(-0.4); //VERDE
            
            /* Saldo positivo: progresso VERDE da ESQUERDA PRA DIREITA.
             * Saldo negativo: progresso VERMELHO da DIREITA PRA ESQUERDA. */
            
            if(prog < 0){
                prog = -prog;
                balColor = Color.RED;
                nodeOr = NodeOrientation.RIGHT_TO_LEFT;
                colorAdj.setHue(1.0); //VERMELHO
            }
            
            Label accName     = new Label(account.getName());
            
            Label accBalance  = new Label(
                                NumberFormat.
                                getCurrencyInstance().
                                format(bal));
            accBalance.setTextFill(balColor);
            
            ProgressBar accPb = new ProgressBar(prog);
            accPb.setNodeOrientation(nodeOr);
            accPb.setEffect(colorAdj);
            
            AnchorPane anchorPane = new AnchorPane();
            
            anchorPane.getChildren().add(accName);
            anchorPane.getChildren().add(accBalance);
            anchorPane.getChildren().add(accPb);
            
            vBox.getChildren().add(anchorPane);
            
            accName   .setLayoutX (14);
            accBalance.setLayoutX (250);
            accPb     .setLayoutX (14);
            accPb     .setPrefWidth(250);
            
            accName   .setLayoutY (14);
            accBalance.setLayoutY (14);
            accPb     .setLayoutY (35);
            
            AnchorPane.setLeftAnchor  (accName   , 14.0);
            AnchorPane.setRightAnchor (accBalance, 14.0);
            AnchorPane.setLeftAnchor  (accPb     , 14.0);
            AnchorPane.setRightAnchor (accPb     , 14.0);
        } 
    }
    
    public void init(FinancesApp app) {
        this.app = app;
        this.app.getUser().addObserver(this);
        this.update(null, null);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        
    }  

    @Override
    public void update(Observable o, Object arg) {        
        this.showAccounts();   
    }
    
}
