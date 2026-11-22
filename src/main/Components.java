import javax.swing.*;

public class Components {
    //Basic Stuff


    public static JFrame frame;
    public static JPanel mainPanel;

    //File reading/writing stuff
    public static JList<PlaceholderItem> objectList;
    public static JLabel selectedFile;
    public static JPanel filePanel;
    public static JPanel fileButtonPanel;
    public static JButton readFile;
    public static JButton writeFile;
    public static JButton refreshFiles;

    //Object making/editing Stuff
    public static JPanel objectPanel;
    public static JLabel objectName;
    public static JTextField nameField;
    public static JButton commitToList;
    public static JButton removeObject;
}