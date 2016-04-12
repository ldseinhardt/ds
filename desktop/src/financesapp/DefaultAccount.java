package financesapp;

public class DefaultAccount extends Account {

    public DefaultAccount(String name) {
        super(name, 0);
    }
    
    public DefaultAccount(String name, double balanceInitial) {
        super(name, balanceInitial);
    }
    
}
