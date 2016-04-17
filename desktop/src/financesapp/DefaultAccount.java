package financesapp;

public class DefaultAccount extends Account {

    public DefaultAccount(String name) {
        super(name, 0);
    }
    
    public DefaultAccount(String name, double openingBalance) {
        super(name, openingBalance);
    }
    
}
