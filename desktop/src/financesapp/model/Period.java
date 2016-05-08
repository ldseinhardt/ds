package financesapp.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Period {
    
    ArrayList<LocalDate> period;
    
    public Period() {
        this.period = new ArrayList();
        this.period.add(LocalDate.MIN.plusDays(1));
        this.period.add(LocalDate.MAX.minusDays(1));
    }
    
    public Period(LocalDate initialDate, LocalDate finalDate){
        this.period = new ArrayList();
        this.period.add(initialDate);
        this.period.add(finalDate);
    }
    
    public Period(int month, int year) {
        this.period = new ArrayList();
        
        //1º dia do mês
        this.period.add(LocalDate.of(year, month, 1));
        
        //Último dia do mês
        this.period.add(LocalDate.of(year, month,
                this.period.get(0).withDayOfMonth(
                this.period.get(0).lengthOfMonth())
               .getDayOfMonth()
        ));
    }
    
    public void setPeriod(LocalDate initialDate, LocalDate finalDate) {
        this.period.clear();
        this.period.add(initialDate);
        this.period.add(finalDate);
    }
    
    public void setPeriod(int month, int year) {
        this.period.clear();
        //1º dia do mês
        this.period.add(LocalDate.of(year, month, 1));
        
        //Último dia do mês
        this.period.add(LocalDate.of(year, month,
                this.period.get(0).withDayOfMonth(
                this.period.get(0).lengthOfMonth())
               .getDayOfMonth()
        ));
    }
    
    public ArrayList<LocalDate> getPeriod() {
        return this.period;
    }
    
    public LocalDate getInitialDate() {
        return this.period.get(0);
    }
    
    public LocalDate getFinalDate() {
        return this.period.get(1);
    }
    
}
