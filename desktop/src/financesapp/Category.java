package financesapp;
 
public abstract class Category {
    
    // Nome da categoria
    protected String name;
    
    public Category(String name) {
        this.name = name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
}
