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

    private Account account;

    private Transaction transaction;

    @FXML
    private BorderPane borderPane;

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @FXML
    private void onSave() {
        if (this.transaction != null) {
            if (this.account == null && !this.transaction.getDescription().equalsIgnoreCase("") && this.transaction.getPayments().size() > 0) {
                //edit
                //this.transaction.setDate();
                //this.transaction.setDescription();
                //this.transaction.setInformation();
                //this.transaction.setCategory();
                //Payments
            } else {
                //add
                //this.transaction.setDate();
                this.transaction.setDescription("teste");
                //this.transaction.setInformation();
                //this.transaction.setCategory();
                this.transaction.addPayments(50, transaction.getDate(), true);
                this.account.addTransaction(transaction);
            }
        }
        this.close();
    }

    @FXML
    private void onCancel() {
        this.close();
    }

    private void close() {
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
        } catch (Exception e) {
            this.accountsView = null;
            this.accountsController = null;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.account = null;
        this.transaction = null;
    }

}
