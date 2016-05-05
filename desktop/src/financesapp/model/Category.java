package financesapp.model;
 
public abstract class Category {
    
    // Nome da categoria
    protected String name;
    
    protected String color;
    
    public Category(String name) {
        this.name = name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setColor(String color){
        this.color = color;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getColor() {
        return this.color;
    }
    
}
