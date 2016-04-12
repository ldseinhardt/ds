package financesapp;
 
import java.time.LocalDate;

public class Recipe extends Transaction {
 
    //Categoria
    private RecipeCategory category;
    
    public Recipe(double value, LocalDate date, int number, boolean concretized, String description, String information) {
        super(value, date, number, concretized, description, information);
    }
    
    public void setCategory(RecipeCategory category) {
        this.category = category;
    }
    
    public RecipeCategory getCategory() {
        return this.category;
    }
    
    public double getRealValue() {
        return this.value * this.number;
    }
    
}
