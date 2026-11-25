import java.io.Serializable;

public class Day implements Serializable {
    private static final long serialVersionUID = 1L;

    private int month;
    private int day;
    private int year;
    private boolean available;

    public Day(int month, int day, int year, boolean available) {
        this.month = month;
        this.day = day;
        this.year = year;
        this.available = available;
    }

    public Day(Day d){
        this.month = d.getMonth();
        this.year = d.getYear();
        this.day = d.getDay();
        this.available = d.isAvailable();
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getDay() {
        return day;
    }

    public boolean isAvailable() {
        return available;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof Day d){
            return day == d.getDay() && month == d.getMonth() && year == d.getYear();
        }
        return false;
    }

    //What my wonderufl beatufil JList Model will show off. How nice.
    @Override
    public String toString() {
        return month+"/"+day+"/"+year + (available ? " - Available" : " - Not available");
    }
}
