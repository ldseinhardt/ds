package financesapp;

abstract class Account {    
    String name;
    private float balance;
    private float balanceInitial;
    
    public String getName() {
        return name;
    }
    
    public float getBalance() {
        return balance;
    }
    
    public float getBalanceInitial() {
        return balanceInitial;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setBalance(float balance) {
        this.balance = balance;
    }
    
    public void setBalanceInitial(float balanceInitial) {
        this.balanceInitial = balanceInitial;
    }    
}
