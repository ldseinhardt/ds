package financesapp.controller;

import financesapp.*;
import financesapp.model.*;
import java.io.*;
import java.net.*;
import java.text.*;
import java.time.LocalDate;
import java.time.format.*;
import java.util.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.util.*;

public class Main implements Initializable, Observer {
    
    //Referência da classe principal
    private FinancesApp app;
    
    //Referências para o relatório de contas
    private Parent accountsView;
    private Accounts accountsController;
    
    //Referências para o formulário de depesa/receita
    private Parent form;
    private TransactionsForm formController;
       
    //Referências para o formulário de transferência
    private Parent transferenceForm;
    private TransferenceForm transferenceFormController;
       
    // Referência para o gráfico de Pizza
    @FXML
    private PieChart typesChart;
    
    @FXML
    private PieChart categoriesChart;
        
    // Dados que serão enviados para o gráfico
    private ObservableList<PieChart.Data> pcData;
    private ObservableList<PieChart.Data> categories;
    
    @FXML
    private BorderPane borderPane;
    
    @FXML
    private ScrollPane expScrollPane;
   
    @FXML
    private ScrollPane incScrollPane;
    
    //Tabela de Transações
    @FXML
    private TableView<Payment> tableView;
    
    @FXML
    private TableColumn dateColunm;    
    
    @FXML 
    private TableColumn descriptionColunm;
    
    @FXML 
    private TableColumn informationColunm;
    
    @FXML       
    private TableColumn valueColunm;
    
    @FXML
    private TableColumn concretizedColunm;
    
    @FXML
    private TableColumn accountColunm;
    
    @FXML
    private TableColumn typeColunm;
    
    @FXML
    private TableColumn categoryColunm;
    
    @FXML
    private Menu menuFilterExpenseCategory;
    
    @FXML
    private Menu menuFilterIncomeCategory;
    
    @FXML
    private Menu menuFilterAccount;
    
    @FXML
    private Menu contextFilterExpenseCategory;
    
    @FXML
    private Menu contextFilterIncomeCategory;
    
    @FXML
    private Menu contextFilterAccount; 
    
    @FXML
    private Label dtIncitial;
    @FXML
    private Label dtFinal;
    @FXML
    private DatePicker dateinicial;
    @FXML
    private DatePicker datefinal;
    @FXML
    private Button Filtrar;
    
    private ObservableList<Payment> transactions;   
    
    private ObservableList<Payment> transferences;
    
    // Filtros
    private ArrayList<String> accountFilter;
    private String typeFilter;
    private String categoryFilter;
    private Period periodFilter;
    
    @FXML
    private Tab transactionsTab;
    
    @FXML
    private void onAddExpense() { 
        if (this.form != null && this.formController != null) { 
            this.showForm();
            this.formController.setTransaction(new Expense());
        }       
    }
    
    @FXML
    private void onAddIncome() {
        if (this.form != null && this.formController != null) { 
            this.showForm();     
            this.formController.setTransaction(new Income());   
        }    
    }
    /*
    @FXML
    private void onAddTransference() {
        if (this.transferenceForm != null && this.transferenceFormController != null) {
            this.showTransferenceForm();
            this.transferenceFormController.setTransference(
                new TransferenceIn(), new TransferenceOut()
            );
        }
    }
    */
    @FXML
    private void onEditTransaction() {
        this.editTransaction(this.tableView.getSelectionModel().getSelectedItem());     
    }
    
    @FXML
    private void onDeleteTransaction() {
        Payment payment = this.tableView.getSelectionModel().getSelectedItem();
        if (payment != null) {
            Transaction transaction = payment.getTransaction();
            transaction.getAccount().getTransactions().remove(transaction);
            this.app.getUser().update();        
        }
    }
    
    @FXML
    private void onDeletePayment() {
        Payment payment = this.tableView.getSelectionModel().getSelectedItem();
        if (payment != null) {
            Transaction transaction = payment.getTransaction();            
            transaction.getPayments().remove(payment);            
            if (transaction.getPayments().isEmpty()) {
                transaction.getAccount().getTransactions().remove(transaction);
            }            
            this.app.getUser().update();        
        }
    }
    
    @FXML
    private void onFilterTypeByExpense() {  
        this.typeFilter = Expense.class.getSimpleName();
        this.showTransactions(new Period());
    }
    
    @FXML
    private void onFilterTypeByIncome() {
        this.typeFilter = Income.class.getSimpleName();
        this.showTransactions(new Period());
    }
    
    @FXML
    private void onFilterTypeByNone() { 
        this.typeFilter = "";
        this.showTransactions(new Period());
    }
    @FXML
    private void onFilterCurrenMonth() {
        this.periodFilter.setPeriod(
                LocalDate.now().getMonthValue(), 
                LocalDate.now().getYear()
        );
        this.showExpensesByCategory(this.periodFilter);
        this.showIncomesByCategory (this.periodFilter);
        this.showTransactions      (this.periodFilter);
    }
    
    @FXML
    private void onFilterIntervalo() { 
        if (this.dateinicial.getValue() == null && this.datefinal.getValue() == null) {
            return;
        }
        
        this.periodFilter.setPeriod(
                this.dateinicial.getValue(),
                this.datefinal.getValue()
        );
        
        this.showExpensesByCategory(this.periodFilter);
        this.showIncomesByCategory (this.periodFilter);
        this.showTransactions      (this.periodFilter);
        
        this.datefinal.setVisible(false);
        this.dateinicial.setVisible(false);
        this.dtFinal.setVisible(false);
        this.dtIncitial.setVisible(false);
        this.Filtrar.setVisible(false);
    }
    
    @FXML
    private void onFilterInterval() {
        this.datefinal.setValue(null);
        this.dateinicial.setValue(null);
        this.datefinal.setVisible(true);
        this.dateinicial.setVisible(true);
        this.dtFinal.setVisible(true);
        this.dtIncitial.setVisible(true);
        this.Filtrar.setVisible(true);
    }
    
    @FXML
    private void onFIlterLastMonth(){
        int lastMonth = LocalDate.now().getMonthValue();
        lastMonth = (lastMonth == 1) ? 12 : lastMonth-1;
        System.out.println(lastMonth);
        this.periodFilter.setPeriod(
                lastMonth,
                LocalDate.now().getYear()
        );
        
        this.showExpensesByCategory(this.periodFilter);
        this.showIncomesByCategory (this.periodFilter);
        this.showTransactions      (this.periodFilter);
    }
    
    @FXML
    private void onFilterAllTime(){
        this.showExpensesByCategory(new Period());
        this.showIncomesByCategory (new Period());
        this.showTransactions      (new Period());    
    }
    
    @FXML
    private void onDeleteCategoryFilter() { 
        this.filterCategory("");   
        this.showTransactions(new Period());
    }
        
    @FXML
    private void onDeleteFilters() {
        this.accountsController.onAccountClicked("");
        this.typeFilter = "";
        this.categoryFilter = "";
        this.showTransactions(new Period());
    }
    
    private void filterCategory(String category) {
        this.categoryFilter = category;
        this.showTransactions(new Period());
    } 
    
    private void filterAccount(String account) {
        this.accountsController.onAccountClicked(account);
        showTransactions(new Period());          
    } 
    
    private void editTransaction(Payment payment) {
        if (this.form != null && this.formController != null && payment != null) {
            this.showForm();
            this.formController.setPayment(payment);
        }
    }
    
    private void showExpensesByCategory(Period period) {
        
        VBox vBox = new VBox(8);
        this.expScrollPane.setContent(vBox);
        
        double max = 0;
        for (Category expCateg : this.app.getExpenseCategories()) {
            double total = this.app.getUser().getTotalByCategory(
                expCateg, Expense.class.getSimpleName(), period
            );
            if (total > max) {
                max = total;
            }
        }
        
        for (Category expCateg : this.app.getExpenseCategories()) {            
            double total = 0;
            
            for (Account acc : this.app.getUser().getAccounts()) {
                if (!this.accountFilter.contains(acc.getName())) {
                    total += acc.getTotalByCategory(expCateg, Expense.class.getSimpleName(), period);
                }
            }
            
            if (total != 0) {
            
                Label lb = new Label(expCateg.getName()
                    + ":   "
                    + NumberFormat.getCurrencyInstance().format(total)
                    + ""
                );
                
                Rectangle rect = new Rectangle();
                
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.getChildren().add(lb);
                anchorPane.getChildren().add(rect);
                
                lb.setLayoutX(14);
                lb.setLayoutY(14);
                lb.setTextFill(expCateg.getColor().darker().darker());
                lb.setFont(new Font(13));
                
                rect.setLayoutX(14);
                rect.setLayoutY(34);
                rect.setHeight(20.0);
                
                // Perfumaria ////////
                rect.setArcHeight(5.0);
                rect.setArcWidth(5.0);
                DropShadow ds = new DropShadow();
                ds.setOffsetY(5.0);
                ds.setOffsetX(5.0);
                ds.setColor(Color.GRAY);
                rect.setEffect(ds);
                rect.setFill(expCateg.getColor());
                //////////////////////
                
                rect.setWidth(total*1000/max);
                
                vBox.getChildren().add(anchorPane);
            }
        }
        
        if (max == 0) {
            Label noData = new Label("Não há dados para exibição");
            vBox.getChildren().add(noData);
        }
        
        vBox.getChildren().add(new Label()); //Espaço vazio
    }
    
    // Soma dos valores de todas as receitas
    private double SumIncome(Period period) {          
        double max = 0;
        
        for (IncomeCategory incCateg : this.app.getIncomeCategories()) {
            max += this.app.getUser().getTotalByCategory(
                incCateg, Income.class.getSimpleName(), period
            );
        }
        
        return max;
    }
    
    // Soma dos valores de todas as despesas
    private double SumExpense(Period period) {
        double max = 0;
        
        for (ExpenseCategory expCateg : this.app.getExpenseCategories()) {
            max += this.app.getUser().getTotalByCategory(
                expCateg, Expense.class.getSimpleName(), period
            );
        }
        
        return max;
    }
        
    private void showIncomesByCategory(Period period) {
        
        VBox vBox = new VBox(8);
        this.incScrollPane.setContent(vBox);
        
        double max = 0;
        for (IncomeCategory incCateg : this.app.getIncomeCategories()) {
            double total = this.app.getUser().getTotalByCategory(
                incCateg, Income.class.getSimpleName(), period
            );
            if (total > max) {
                max = total;
            }
        }
        
        for (IncomeCategory incCateg : this.app.getIncomeCategories()) {     
            double total = 0;
            
            for (Account acc : this.app.getUser().getAccounts()) {
                if (!this.accountFilter.contains(acc.getName())) {
                    total += acc.getTotalByCategory(incCateg, Income.class.getSimpleName(), period);
                }
            }
            
            if (total != 0) {
                
                Label lb = new Label(incCateg.getName()
                    + ":   "
                    + NumberFormat.getCurrencyInstance().format(total)
                    + ""
                );
                
                Rectangle rect = new Rectangle();
                
                AnchorPane anchorPane = new AnchorPane();
                anchorPane.getChildren().add(lb);
                anchorPane.getChildren().add(rect);
                
                lb.setLayoutX(14);
                lb.setLayoutY(14);
                lb.setTextFill(incCateg.getColor().darker().darker());
                lb.setFont(new Font(13));
                
                rect.setLayoutX(14);
                rect.setLayoutY(34);
                rect.setHeight(20.0);
                
                // Perfumaria ////////
                rect.setArcHeight(5.0);
                rect.setArcWidth(5.0);
                DropShadow ds = new DropShadow();
                ds.setOffsetY(5.0);
                ds.setOffsetX(5.0);
                ds.setColor(Color.GRAY);
                rect.setEffect(ds);
                rect.setFill(incCateg.getColor());
                //////////////////////
                
                rect.setWidth(total*1000/max);
                
                vBox.getChildren().add(anchorPane);
            }
        }    
        
        if (max == 0) {
            Label noData = new Label("Não há dados para exibição");
            vBox.getChildren().add(noData);
        }
        
        vBox.getChildren().add(new Label()); //Espaço vazio
    }
    
    private void showForm() {
        this.app.getUser().deleteObserver(this.accountsController); 
        this.borderPane.setLeft(this.form);      
    }
    
    private void showTransferenceForm() {
        this.app.getUser().deleteObserver(this.accountsController);
        this.borderPane.setLeft(this.transferenceForm);
    }
    
    @FXML
    private void onAccountsGestor() {          
        this.app.showAccountsGestor();    
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
    private void onNew() {
        this.app.newFile();
    }  
    
    @FXML
    private void onExit() {
        this.app.exit();
    }   
    
    public void init(FinancesApp app) {
        this.app = app;
        this.update(null, null);
        this.accountsController.init(this.app);
        this.formController.init(this.app);   
        this.transferenceFormController.init(this.app);
        
        for (Category category : this.app.getExpenseCategories()) {
            MenuItem menuItem = new MenuItem(category.getName());   
            menuItem.setOnAction(e ->
                this.filterCategory(category.getName())
            );
            this.menuFilterExpenseCategory.getItems().add(menuItem);
            
            MenuItem menuItemContext = new MenuItem(category.getName());   
            menuItemContext.setOnAction(e ->
                this.filterCategory(category.getName())
            ); 
            this.contextFilterExpenseCategory.getItems().add(menuItemContext);            
        }
        
        for (Category category : this.app.getIncomeCategories()) {
            MenuItem menuItem = new MenuItem(category.getName());   
            menuItem.setOnAction(e ->
                this.filterCategory(category.getName())
            );
            this.menuFilterIncomeCategory.getItems().add(menuItem);
            
            MenuItem menuItemContext = new MenuItem(category.getName());   
            menuItemContext.setOnAction(e ->
                this.filterCategory(category.getName())
            );
            this.contextFilterIncomeCategory.getItems().add(menuItemContext);          
        }
        
        this.accountsController.getAccountsFilter().addObserver(this);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.accountFilter = new ArrayList<>();
        this.typeFilter = "";
        this.categoryFilter = "";
        this.periodFilter = new Period();
         
        this.dateColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();                  
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");              
                property.setValue(formatter.format(payment.getValue().getDate()));
                return property;      
            }
        });
        
        this.dateColunm.setComparator(new Comparator<String>() {
            @Override 
            public int compare(String a, String b) {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Date d1 = format.parse(a);                
                    Date d2 = format.parse(b);
                    return Long.compare(d1.getTime(), d2.getTime());
                } catch(Exception e) {
                    
                }                
                return -1;
            }
        });
        
        this.descriptionColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();                  
                property.setValue(payment.getValue().getTransaction().getDescription());
                return property;      
            }
        });
        
        this.informationColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();                  
                property.setValue(payment.getValue().getTransaction().getInformation());
                return property;      
            }
        });
        
        this.valueColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();
                NumberFormat formatter = NumberFormat.getCurrencyInstance(); 
                property.setValue(formatter.format(payment.getValue().getValue()));
                return property;      
            }
        });
        
        this.valueColunm.setCellFactory(c -> {
            return new TableCell<Payment, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);                        
                    if (empty) {   
                        this.setText(""); 
                        this.setGraphic(null);
                    } else {
                        this.setText(item);                        
                        TableRow<Payment> row = this.getTableRow();
                        row.getStyleClass().removeAll(
                            "expense-text-color",
                            "income-text-color"
                        );
                        if (row.getItem() != null) {
                            row.getStyleClass().add(
                                (row.getItem().getTransaction() instanceof Expense)
                                    ? "expense-text-color"
                                    : "income-text-color"
                            );
                        }
                    }  
                }
            };
        });
        
        this.concretizedColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();
                property.setValue(payment.getValue().hasConcretized() ? "Sim" : "Não");
                return property;      
            }
        });
        
        this.accountColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();
                property.setValue(payment.getValue().getTransaction().getAccount().getName());
                return property;      
            }
        });
        
        this.typeColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();
                property.setValue(
                    (payment.getValue().getTransaction() instanceof Expense)
                        ? "Despesa"
                        : "Receita"
                );
                return property;      
            }
        });
        
        this.categoryColunm.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Payment, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Payment, String> payment) {
                SimpleStringProperty property = new SimpleStringProperty();
                property.setValue(payment.getValue().getTransaction().getCategory().getName());
                return property;      
            }
        });
        
        this.tableView.setRowFactory(tv -> {
            TableRow<Payment> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (e.getClickCount() == 2 && (!row.isEmpty())) {
                    Payment payment = row.getItem();
                    this.editTransaction(payment);
                }
            });
            return row;
        });
        
        this.transactions = FXCollections.observableArrayList();
        
        this.tableView.setItems(this.transactions); 

        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getClass().getResource("/financesapp/view/Accounts.fxml")
            );
            this.accountsView = loader.load();        
            this.accountsController = loader.getController();
            this.borderPane.setLeft(this.accountsView);            
        } catch(Exception e) {
            this.accountsView = null;
            this.accountsController = null;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/financesapp/view/TransactionsForm.fxml")
            );
            this.form = loader.load();
            this.formController = loader.getController();
        } catch(Exception e) {
            this.form = null;
            this.formController = null;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/financesapp/view/TransferenceForm.fxml")
            );
            this.transferenceForm = loader.load();
            this.transferenceFormController = loader.getController();
        } catch(Exception e) {
            this.transferenceForm = null;
            this.transferenceFormController = null;
        }
    }
    
    public void showTransactions(Period period) {
        this.transactions.clear();
        
        for (Account account : this.app.getUser().getAccounts()) {
            if (this.accountFilter.contains(account.getName())) {
                continue;
            }
            
            for (Payment payment : account.getPayments(period)) {
                if (!this.typeFilter.isEmpty() && !payment.getTransaction().getClass().getSimpleName().equalsIgnoreCase(this.typeFilter)) {
                    continue;
                }                
                if (!this.categoryFilter.isEmpty() && !payment.getTransaction().getCategory().getName().equalsIgnoreCase(this.categoryFilter)) {
                    continue;
                }
                this.transactions.add(payment);
            }
        }
    }
    
    public void showTransfrence(Period period) {
        this.transactions.clear();
        
        for (Account account : this.app.getUser().getAccounts()) {
            if (this.accountFilter.contains(account.getName())) {
                continue;
            }
            
            for (Payment payment : account.getPayments(period)) {
                if (!this.typeFilter.isEmpty() && !payment.getTransaction().getClass().getSimpleName().equalsIgnoreCase(this.typeFilter)) {
                    continue;
                }                
                if (!this.categoryFilter.isEmpty() && !payment.getTransaction().getCategory().getName().equalsIgnoreCase(this.categoryFilter)) {
                    continue;
                }
                this.transactions.add(payment);
            }
        }
    }
    
    @Override
    public void update(Observable o, Object arg) {  
        // Mês atual /////////////////////
        LocalDate first = LocalDate.of(
            LocalDate.now().getYear(),
            LocalDate.now().getMonthValue(),
            1
        );
        LocalDate last = first.withDayOfMonth(
            first.lengthOfMonth()
        );
        //////////////////////////////////
        
        double expTotal = this.SumExpense(new Period());
        double incTotal = this.SumIncome(new Period());
        
        //if (expTotal != 0 || incTotal != 0) {
            pcData = FXCollections.observableArrayList();
            pcData.add(new PieChart.Data("Despesas", expTotal));
            pcData.add(new PieChart.Data("Receitas", incTotal));
            typesChart.setData(pcData);
            typesChart.setTitle("Despesas vs Receitas");
            
            // Mostra valores na tela do Gráfico de Pizza - Receita X Despesa
            pcData.forEach(data ->
                data.nameProperty().bind(
                    Bindings.concat(
                        data.getName(),
                        " ",
                        NumberFormat.getCurrencyInstance().format(data.pieValueProperty().getValue())
                    )
                )
            );
            
            categories = FXCollections.observableArrayList();
            for (ExpenseCategory categ : this.app.getExpenseCategories()) {
                categories.add(
                    new PieChart.Data(
                        categ.getName(), this.app.getUser().getTotalByCategory(
                            categ, Expense.class.getSimpleName(), new Period()
                        )
                    )
                );
            }
            categoriesChart.setData(categories);
            categoriesChart.setTitle("Categorias");
            
            // Mostra valores na tela do Gráfico de Pizza ( PieChart )  de Categorias
            categories.forEach(data ->
                data.nameProperty().bind(
                    Bindings.concat(
                        data.getName(),
                        " ",
                        NumberFormat.getCurrencyInstance().format(data.pieValueProperty().getValue())
                    )
                )
            );
            //////////////////////////////////////////
        /*}
        else {
            Label noData = new Label("Não há dados para exibição");
            //Não pode destruir os gráficos da interface
            this.graficos.setContent(noData);
        }*/
        this.menuFilterAccount.getItems().clear();            
        MenuItem allAcocunts = new MenuItem("Todas as Contas");   
        allAcocunts.setOnAction(e -> this.filterAccount(""));
        this.menuFilterAccount.getItems().add(allAcocunts);
        
        this.contextFilterAccount.getItems().clear();
        MenuItem allAcocuntsContext = new MenuItem("Todas as Contas");   
        allAcocuntsContext.setOnAction(e -> this.filterAccount(""));
        this.contextFilterAccount.getItems().add(allAcocuntsContext);
        
        for (Account account : this.app.getUser().getAccounts()) {
            MenuItem menuItem = new MenuItem(account.getName());   
            menuItem.setOnAction(e ->
                this.filterAccount(account.getName())
            );
            this.menuFilterAccount.getItems().add(menuItem);
            
            MenuItem menuItemContext = new MenuItem(account.getName());   
            menuItemContext.setOnAction(e ->
                this.filterAccount(account.getName())
            );
            this.contextFilterAccount.getItems().add(menuItemContext);
        }
        
        this.accountFilter.clear();
        this.accountFilter.addAll(this.accountsController.getAccountsFilter().getValues());
        
        //this.showExpensesByCategory(new Period(first, last));
        //this.showIncomesByCategory(new Period(first, last));
        //this.showTransactions(new Period(firdst, last));
        this.showExpensesByCategory(this.periodFilter);
        this.showIncomesByCategory (this.periodFilter);
        this.showTransactions      (this.periodFilter);
    }
    
}
