package financesapp.controller;

import financesapp.*;
import financesapp.model.*;
import java.net.URL;
import java.text.*;
import java.time.LocalDate; //java.time.* tem uma classe "Period"
import java.util.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.*;

public class Accounts implements Initializable, Observer {
    
    //Referência da classe principal
    private FinancesApp app;
    
    @FXML
    private ScrollPane scrollPane;

    private static accountFilterObvervable accountsFilter;

    public void onAccountClicked(String account){
        Accounts.accountsFilter.setValue(account);
        
        showAccounts();
    }

    public accountFilterObvervable getAccountsFilter(){
        return Accounts.accountsFilter;
    }
    
    private void showAccounts() {
        VBox vBox = new VBox();
        vBox.getStyleClass().add("sideBar");
        vBox.setMaxWidth(300);
        this.scrollPane.setContent(vBox);
        
        Label title = new Label("Contas");
        title.setFont(new Font(24));
        
        vBox.getChildren().add(title);        
        
        double bal, prog, generalMaxBalance;
        Color balColor;
        NodeOrientation nodeOr;
        ColorAdjust colorAdj;
        
        generalMaxBalance = this.app.getUser().getGeneralMaxBalance(
            new Period(
                LocalDate.now().getMonthValue(),
                LocalDate.now().getYear()
            )
        );
        
        for (Account account : this.app.getUser().getAccounts()) {            
            bal      = account.getBalance(LocalDate.now());
            balColor = Color.GREEN;
            prog     = bal/(generalMaxBalance*1.05);
            nodeOr   = NodeOrientation.LEFT_TO_RIGHT;
            colorAdj = new ColorAdjust();
            colorAdj.setHue(-0.4); //VERDE
            
            /* Saldo positivo: progresso VERDE da ESQUERDA PRA DIREITA.
             * Saldo negativo: progresso VERMELHO da DIREITA PRA ESQUERDA. */
            
            if (prog < 0) {
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
            accBalance.setPrefWidth(150);
            accBalance.setAlignment(Pos.CENTER_RIGHT);
            accBalance.setTextFill(balColor);
            
            ProgressBar accPb = new ProgressBar(prog);
            accPb.setNodeOrientation(nodeOr);
            accPb.setEffect(colorAdj);
            accPb.setPrefWidth(Double.MAX_VALUE);
            accPb.setOnMouseClicked((MouseEvent event)
                    -> onAccountClicked(account.getName())
            );
            
            HBox hBox = new HBox(10);
            hBox.getStyleClass().add("customHBox");
            hBox.getChildren().add(accName);         
            hBox.getChildren().add(accBalance);            
            
            // VBox que funciona como botão toggle
            VBox accButton = new VBox(5);
            if(accountsFilter.values.contains(account.getName())) {
                accButton.getStyleClass().add("customVBox");
                accName.getStyleClass().add("unselected");
                accBalance.getStyleClass().add("unselected");
                accPb.getStyleClass().add("unselected");
            }
            else {
                accButton.getStyleClass().add("customVBoxClicked");
            }

            accButton.getChildren().add(hBox);
            accButton.getChildren().add(accPb);
            accButton.setOnMouseClicked((MouseEvent event)
                    -> onAccountClicked(account.getName())
            );
            
            vBox.getChildren().add(accButton);
            vBox.getChildren().add(new Label(""));
        } 
        
        double totalBalance = this.app.getUser().getBalance(LocalDate.now());
        
        Label st = new Label("Saldo total: ");
        Label total = new Label(NumberFormat.getCurrencyInstance().format(
            totalBalance
        ));
        
        total.setTextFill((totalBalance < 0)
            ? Color.RED
            : Color.GREEN
        );
               
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
        this.scrollPane.setFitToWidth(true);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {  
        Accounts.accountsFilter = new accountFilterObvervable();
    }  

    @Override
    public void update(Observable o, Object arg) {        
        this.showAccounts();   
    }
    
}

class accountFilterObvervable extends Observable {
    ArrayList<String> values;
    
    public accountFilterObvervable(){
        this.values = new ArrayList();
    }
    
    public void setValue(String str) {
        if (str.isEmpty()) {
            values.clear();
        } else if (values.contains(str)) {
            values.remove(str);
        } else {
            values.add(str);
        }
                
        this.setChanged();
        this.notifyObservers();
    }
    
    public ArrayList<String> getValues() {
        return this.values;
    }
}
