package financesapp.interfaces;

import financesapp.*;
import java.io.File;
import java.net.*;
import java.util.*;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.stage.*;

public class MainController implements Initializable, Observer {
    
    //Referência da classe principal
    private FinancesApp app;
    
    //Referências para o formulário
    private Parent accountsView;
    private AccountsController accountsController;
    
    @FXML
    private BorderPane borderPane;
    
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
    
    public void init(FinancesApp app) {
        this.app = app;        
        this.update(null, null);  
        this.accountsController.init(this.app);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {       
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("AccountsView.fxml")
            );
            this.accountsView = loader.load();
            this.accountsController = loader.getController();
            this.borderPane.setLeft(this.accountsView);            
        } catch(Exception e) {
            this.accountsView = null;
            this.accountsController = null;
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {
        
    }
    
}
