import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static Path currentFile;
    public static DefaultListModel<Calender> calendarModel = new DefaultListModel<>();
    public static DefaultListModel<Day> dayModel = new DefaultListModel<>();
    public static Calender selectedCalendar;
    public static Day selectedDay;
    public static int currentIndex;

    //Main! all on the EDT.
    public static void main(String[] args){

        SwingUtilities.invokeLater(() -> {

            setUpJavaSwing();
            setUpFileReadWritePanel();
            setUpCalendarEditPanel();
            setUpDayEditPanel();
            resultsSetUp();
        });
    }

    //The basic frames and stuff  that we need
    public static void setUpJavaSwing(){

        Components.frame = new JFrame("Calendar App");
        Components.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Components.frame.setSize(750, 1250);


        Components.mainPanel = new JPanel();
        Components.mainPanel.setLayout(new GridLayout(4,1));

        Components.frame.add(Components.mainPanel);

        Components.frame.setVisible(true);
    }

    /*
    This sets up those first 4 things, the JList of calendars, reset, load, and save.
    Pretty  shrimple.
     */
    public static void setUpFileReadWritePanel() {
        Components.selectedFile = new JLabel("No selection", SwingConstants.CENTER);
        Components.readFile = new JButton("Load File");
        Components.writeFile = new JButton("Save File");
        Components.refreshFiles = new JButton("Reload");


        Components.calendarList = new JList<>(calendarModel);
        Components.calendarList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(Components.calendarList);

        Components.calendarList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedCalendar = Components.calendarList.getSelectedValue();
                currentIndex = Components.calendarList.getSelectedIndex();
                refreshDayList();

            }
        });

        Components.readFile.addActionListener(e -> chooseAndLoad(calendarModel));
        Components.refreshFiles.addActionListener(e -> {
            RefreshObjList();
        });
        Components.writeFile.addActionListener(e -> saveFile(calendarModel));


        Components.filePanel = new JPanel(new BorderLayout());
        Components.filePanel.add(Components.selectedFile, BorderLayout.NORTH);
        Components.filePanel.add(scrollPane, BorderLayout.CENTER);

        Components.fileButtonPanel = new JPanel(new GridLayout(3, 1));
        Components.fileButtonPanel.add(Components.readFile);
        Components.fileButtonPanel.add(Components.writeFile);
        Components.fileButtonPanel.add(Components.refreshFiles);
        Components.filePanel.add(Components.fileButtonPanel, BorderLayout.SOUTH);

        Components.mainPanel.add(Components.filePanel);
    }
/*
This does all the stuff needed to select and add/remove days from your
selected calendar object. the JList is sorted BC addDays keeps it all clean.

 */
public static void setUpDayEditPanel(){
    Components.dayList = new JList<>(dayModel);
    Components.dayList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    Components.editDays = new JPanel(new GridLayout(1, 2));
    JScrollPane dayScrollPanel = new JScrollPane(Components.dayList);
    Components.editDays.add(dayScrollPanel);
    Components.dayList.addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            selectedDay = Components.dayList.getSelectedValue();
            if(selectedDay != null) {
                Components.dayField.setText("" + selectedDay.getDay());
                Components.monthField.setText("" + selectedDay.getMonth());
                Components.yearField.setText("" + selectedDay.getYear());
                Components.availField.setSelected(selectedDay.isAvailable());
            }
        }});

    // Grid is now 6 rows because we added one more button
    Components.dayentryPanel = new JPanel(new GridLayout(6,2));

    Components.addDay = new JButton("Add Day");
    Components.removeDay = new JButton("remove Day");
    Components.fillDates = new JButton("Fill Today's Date");

    Components.month = new JLabel("Enter Month of date");
    Components.monthField = new JTextField(20);
    Components.dayentryPanel.add(Components.month);
    Components.dayentryPanel.add(Components.monthField);
    Components.day = new JLabel("Enter Day of date");
    Components.dayField = new JTextField();
    Components.dayentryPanel.add(Components.day);
    Components.dayentryPanel.add(Components.dayField);
    Components.year = new JLabel("Enter year of date");
    Components.yearField = new JTextField();
    Components.dayentryPanel.add(Components.year);
    Components.dayentryPanel.add(Components.yearField);
    Components.available = new JLabel("Enter availability of date");
    Components.availField = new JCheckBox();
    Components.dayentryPanel.add(Components.available);
    Components.dayentryPanel.add(Components.availField);

    Components.dayentryPanel.add(Components.fillDates);
    Components.dayentryPanel.add(new JLabel()); // empty cell to balance the grid
    Components.dayentryPanel.add(Components.addDay);
    Components.dayentryPanel.add(Components.removeDay);
    Components.editDays.add(Components.dayentryPanel);
    Components.mainPanel.add(Components.editDays);

    Components.fillDates.addActionListener(e -> {
        LocalDate today = LocalDate.now();
        Components.monthField.setText(String.valueOf(today.getMonthValue()));
        Components.dayField.setText(String.valueOf(today.getDayOfMonth()));
        Components.yearField.setText(String.valueOf(today.getYear()));
    });

    Components.addDay.addActionListener(e -> {
        if (selectedCalendar == null) return;
        try {
            selectedCalendar.addDay(createDay());
            refreshDayList();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Month, day, and year must be whole numbers.");
        }
    });

    Components.removeDay.addActionListener(e -> {
        if (selectedCalendar == null || selectedDay == null) return;
        selectedCalendar.removeDay(selectedDay);
        selectedDay = null;
        refreshDayList();
    });
}

    /*
    returns a day created based on the day text fields. The add buttons
    action listener catches the errors from parsing.
   */
    private static Day createDay(){
        return new Day(Integer.parseInt(Components.monthField.getText()),
                Integer.parseInt(Components.dayField.getText()),
                Integer.parseInt(Components.yearField.getText()),
                Components.availField.isSelected());

    }

    /*
    Ngl I used ai to help with these next 4 file reading/writing methods.

    So this one assumes that the various saved lists of calendar is in a
    'files' folder. and return the path to it.
     */
    private static Path getFilesDirectory() {
        Path dir = Paths.get("files");
        try {
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Could not create files directory: " + e.getMessage());
        }
        return dir;
    }

    //I didnt know about JFileChooser and this is very convienent.
    private static void chooseAndLoad(DefaultListModel<Calender> model) {
        JFileChooser chooser = new JFileChooser(getFilesDirectory().toFile());
        chooser.setCurrentDirectory(getFilesDirectory().toFile());
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            loadFile(model, chooser.getSelectedFile().toPath());
        }
    }

    //Sets up the Calendar list model with all the calendar in a selected file.
    private static void loadFile(DefaultListModel<Calender> model, Path path) {
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
            List<Calender> items = (List<Calender>) in.readObject();
            model.clear();
            for (Calender item : items) {
                model.addElement(item);
            }
            currentFile = path;
            Components.selectedFile.setText(currentFile.getFileName().toString());
        } catch (IOException | ClassNotFoundException | ClassCastException ex) {
            JOptionPane.showMessageDialog(null, "Could not load file: " + ex.getMessage());
        }
    }

    /*
    We saving to a file using File chooser. Each calendar is serializable and gets
    saved as an object.
     */
    private static void saveFile(DefaultListModel<Calender> model) {
        Path path = currentFile;

        JFileChooser chooser = new JFileChooser(getFilesDirectory().toFile());
        chooser.setCurrentDirectory(getFilesDirectory().toFile());
        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        path = chooser.getSelectedFile().toPath();

        List<Calender> items = new ArrayList<>(Collections.list(model.elements()));
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(path))) {
            out.writeObject(items);
            currentFile = path;
            JOptionPane.showMessageDialog(null, "Saved to " + path.getFileName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Could not save file: " + ex.getMessage());
        }
        Components.selectedFile.setText(currentFile.getFileName().toString());
    }

    /*
    The JSwing, buttons, and that stuff for adding/removing calendars.
     */
    public static void setUpCalendarEditPanel() {
        Components.calendarPanel = new JPanel(new GridLayout(2, 2));
        Components.calendarNameLabel = new JLabel("Create name for calendar");


        Components.nameField = new JTextField(40);

        Components.commitToList = new JButton("Add this Calendar to file");
        Components.removeObject = new JButton("Delete This Calendars data from the file");

        Components.commitToList.addActionListener( e -> {
            if(Components.nameField.getText() != null && !Components.nameField.getText().equals("")) {
                calendarModel.addElement(createCalendar());
                Components.calendarNameLabel.setText("Calendar Added.");
            } else{ Components.calendarNameLabel.setText("Improper Calender name.");}
        });

        Components.removeObject.addActionListener( e -> {
            calendarModel.removeElement(selectedCalendar);
        });

        Components.calendarPanel.add(Components.calendarNameLabel);
        Components.calendarPanel.add(Components.nameField);
        Components.calendarPanel.add(Components.commitToList);
        Components.calendarPanel.add(Components.removeObject);
        Components.mainPanel.add(Components.calendarPanel);

    }

    //Resets the current page basically.
    public static void RefreshObjList(){
        if (currentFile != null) {
            loadFile(calendarModel, currentFile);
        }
    }

    // makes it so the model has only the days shown that are in the selected calendar.
    public static void refreshDayList() {
        dayModel.clear();
        selectedDay = null;
        if (selectedCalendar != null) {
            for (Day item : selectedCalendar.getDays()) {
                dayModel.addElement(item);
            }
        }
        Components.mainPanel.repaint();
    }

    //Makes a calendar using calender text fields.
    public static Calender createCalendar(){

        return new Calender(Components.nameField.getText());

    }

    //Final part of results stuff
    public static void resultsSetUp() {
        Components.resultsPanel = new JPanel(new GridLayout(1, 2));

        Components.bestDaysToMeet = new JList<>();
        Components.getBestDays = new JButton("Get Result list");

        Components.resultsPanel.add(new JScrollPane(Components.bestDaysToMeet));
        Components.resultsPanel.add(Components.getBestDays);

        Components.getBestDays.addActionListener(e -> {
            List<Calender> calendars = new ArrayList<>();

            for (int i = 0; i < calendarModel.getSize(); i++) {
                calendars.add(calendarModel.getElementAt(i));
            }

            Components.bestDaysToMeet.setListData(
                    MeetingPlanner.rankBestDays(calendars).toArray(new String[0])
            );
        });

        Components.mainPanel.add(Components.resultsPanel);
    }







}