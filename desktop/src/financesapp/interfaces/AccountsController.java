package financesapp.interfaces;

import financesapp.*;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;
import javafx.fxml.*;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class AccountsController implements Initializable, Observer {
    
    //Referência da classe principal
    private FinancesApp app;
    
    @FXML
    private ScrollPane scrollPane;

    private void showAccounts() {
        VBox vBox = new VBox(3);
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setAlignment(Pos.CENTER);
        this.scrollPane.setContent(vBox);
        
        Label title = new Label("Contas");
        title.setFont(new Font(24));
        
        vBox.getChildren().add(title);        
        
        double bal, prog, generalMaxBalance;
        Color balColor = Color.GREEN;
        NodeOrientation nodeOr;
        ColorAdjust colorAdj;
        
        generalMaxBalance = this.app.getUser().getGeneralMaxBalance();
        Iterator<Account> ac = this.app.getUser().getAccounts().iterator();
        while (ac.hasNext()) {
            
            Account account = ac.next();
            
            bal      = account.getBalance();
            balColor = Color.GREEN;
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
            
            Label accName = new Label(account.getName());
            accName.setPrefWidth(150);
            
            Label accBalance = new Label(
                NumberFormat.getCurrencyInstance().format(bal)
            );
            accBalance.setPrefWidth(100);
            accBalance.setAlignment(Pos.CENTER_RIGHT);
            accBalance.setTextFill(balColor);
            
            ProgressBar accPb = new ProgressBar(prog);
            accPb.setNodeOrientation(nodeOr);
            accPb.setEffect(colorAdj);
            accPb.setPrefWidth(260);
            
            HBox hBox = new HBox(5);            
            hBox.getChildren().add(accName);            
            hBox.getChildren().add(accBalance);            
            
            vBox.getChildren().add(hBox);
            vBox.getChildren().add(accPb);
            vBox.getChildren().add(new Label()); //Espaço entre contas
        } 
        
        double totalBalance = this.app.getUser().getBalance();
        
        Label st = new Label("Saldo total: ");
        Label total = new Label(NumberFormat.getCurrencyInstance().format(
            this.app.getUser().getBalance()
        ));
        
        if(totalBalance < 0)
            total.setTextFill(Color.RED);
        else
            total.setTextFill(Color.GREEN);
        
        HBox hBox = new HBox();
        hBox.getChildren().add(st);
        hBox.getChildren().add(total);
        hBox.setAlignment(Pos.CENTER);
        hBox.setScaleY(1.3);
        
        vBox.getChildren().add(hBox);
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
