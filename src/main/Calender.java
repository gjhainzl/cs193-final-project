import java.io.Serializable;
import java.util.ArrayList;

public class Calender implements Serializable {
    private static final long serialVersionUID = 1L;

    private String OwnersName;
    private ArrayList<Day> days;
    private int daysInCalendar;


    public Calender(String name) {
        this.OwnersName = name;
        this.days = new ArrayList<>();
        daysInCalendar = 0;
    }

    public int getDaysInCalendar(){
        return daysInCalendar;
    }

    public String getOwnersName() {
        return OwnersName;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    // Adds a day only if it isn't already in the calendar, adding days in date order
    public boolean addDay(Day toAdd) {
        if (findDay(toAdd)) {
            return false;
        }

        Day newDay = new Day(toAdd);
        int index = 0;
        while (index < days.size() && isBefore(days.get(index), newDay)) {
            index++;
        }

        days.add(index, newDay);
        daysInCalendar++;
        return true;
    }

    // Returns true if date a comes before date b
    private boolean isBefore(Day a, Day b) {
        if (a.getYear() != b.getYear()) {
            return a.getYear() < b.getYear();
        }
        if (a.getMonth() != b.getMonth()) {
            return a.getMonth() < b.getMonth();
        }
        return a.getDay() < b.getDay();
    }


    //returns if the removed thing worked.
    public boolean removeDay(Day toRemove) {
           return days.remove(toRemove);
    }

    //return true if a day is in this calendar. False if not.
    public boolean findDay(Day date) {
        for (Day d : days) {
            if (d.equals(date)) {
                return true;
            }
        }
        return false;
    }

    // this is what the JList displays
    @Override
    public String toString() {
        return OwnersName +"'s Calendar";
    }
}
