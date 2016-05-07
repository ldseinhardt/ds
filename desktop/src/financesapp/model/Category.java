package financesapp.model;
 
import javafx.scene.paint.Color;

public abstract class Category {
    
    // Nome da categoria
    protected String name;
    
    protected Color color;
    
    public Category(String name) {
        this.name = name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Color getColor() {
        return this.color;
    }
    
}
