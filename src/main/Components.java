import javax.swing.*;

public class Components {
    //Basic Stuff


    public static JFrame frame;
    public static JPanel mainPanel;

    //File reading/writing stuff
    public static JList<Calender> calendarList;
    public static JLabel selectedFile;
    public static JPanel filePanel;
    public static JPanel fileButtonPanel;
    public static JButton readFile;
    public static JButton writeFile;
    public static JButton refreshFiles;

    //Calendar making/editing Stuff
    public static JPanel calendarPanel;
    public static JLabel calendarNameLabel;
    public static JTextField nameField;
    public static JButton commitToList;
    public static JButton removeObject;

    //adding/removing individiual days in calendar
    public static JList<Day> dayList;
    public static JTextField monthField;
    public static JLabel month;
    public static JTextField dayField;
    public static JLabel day;
    public static JTextField yearField;
    public static JLabel year;
    public static JCheckBox availField;
    public static JLabel available;
    public static JButton addDay;
    public static JButton removeDay;
    public static JButton fillDates;
    public static JPanel editDays;
    public static JPanel dayentryPanel;

    //Getting results from all calendars
    public static JPanel resultsPanel;
    public static JList<String> bestDaysToMeet;
    public static JButton getBestDays;
}