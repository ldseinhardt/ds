package financesapp.interfaces;

import financesapp.*;
import java.io.File;
import java.net.*;
import java.text.*;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class MainController implements Initializable, Observer {
    
    //Referência da classe principal
    private FinancesApp app;
    
    @FXML
    private AnchorPane accounts;

    private void showAccounts() {
        accounts.getChildren().clear();
        
        int y = 0;
        double bal, prog;
        Color balColor;
        NodeOrientation nodeOr;
        ColorAdjust colorAdj;
        
        Iterator<Account> ac = this.app.getUser().getAccounts().iterator();
        while (ac.hasNext()) {
            
            Account account = ac.next();
            
            bal      = account.getBalance();
            balColor = Color.DARKGREEN;
            prog     = bal/500; //R$ 500: teste. 100% sera o maior saldo no mes.
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
            
            accounts.getChildren().add(accName);
            accounts.getChildren().add(accBalance);
            accounts.getChildren().add(accPb);
            
            accName   .setLayoutX (14);
            accBalance.setLayoutX (200);
            accPb     .setLayoutX (14);
            accPb     .setPrefWidth(200);
            
            accName   .setLayoutY (14 + y);
            accBalance.setLayoutY (14 + y);
            accPb     .setLayoutY (35 + y);
            
            AnchorPane.setLeftAnchor  (accName   , 14.0);
            AnchorPane.setRightAnchor (accBalance, 14.0);
            AnchorPane.setLeftAnchor  (accPb     , 14.0);
            AnchorPane.setRightAnchor (accPb     , 14.0);
            y += 50;
        } 
    }
    
    @FXML
    private void onOpen() {          
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecione o arquivo...");
        FileChooser.ExtensionFilter extFilter =
            new FileChooser.ExtensionFilter("Arquivo JSON", "*.json", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(this.app.getWindow());
        if (file != null) {
            this.app.open(file.getAbsoluteFile().toString());
        }
    }
    
    @FXML
    private void onSave() {          
        this.app.save();
    }
    
    @FXML
    private void onSaveAs() {          
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar arquivo...");
        FileChooser.ExtensionFilter extFilter =
            new FileChooser.ExtensionFilter("Arquivo JSON", "*.json", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialFileName(this.app.getDefaultFileName());
        File file = fileChooser.showSaveDialog(this.app.getWindow());
        if (file != null) {        
            this.app.save(file.getAbsoluteFile().toString()); 
        }
    }
    
    @FXML
    private void onAccountsGestor() {          
        this.app.showAccountsGestor();    
    }
    
    public void init(FinancesApp app) {
        this.app = app;        
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
