import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.*;
import java.nio.file.*;




public class Main {
    public static Path currentFile;
    public static DefaultListModel<PlaceholderItem> model = new DefaultListModel<>();
    public static PlaceholderItem selected;
    public static int currentIndex;
    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            setUpJavaSwing();
            setUpFileReadWritePanel();
            setUpObjectMakePanel();
        });
    }

    public static void setUpJavaSwing(){

        Components.frame = new JFrame("Calendar App");
        Components.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Components.frame.setSize(1000, 1000);


        Components.mainPanel = new JPanel();
        Components.mainPanel.setLayout(new GridLayout(3,2));


        Components.frame.add(Components.mainPanel);

        Components.frame.setVisible(true);
    }

    public static void setUpFileReadWritePanel() {
        Components.selectedFile = new JLabel("No selection", SwingConstants.CENTER);
        Components.readFile = new JButton("Load File");
        Components.writeFile = new JButton("Save File");
        Components.refreshFiles = new JButton("Refresh");


        Components.objectList = new JList<>(model);
        Components.objectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(Components.objectList);

        Components.objectList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selected = Components.objectList.getSelectedValue();
                if(selected!=null) {
                    currentIndex = Components.objectList.getSelectedIndex();
                    Components.objectName.setText(selected.toString());
                }
            }
        });

        Components.readFile.addActionListener(e -> chooseAndLoad(model));
        Components.refreshFiles.addActionListener(e -> {
            RefreshObjList();
        });
        Components.writeFile.addActionListener(e -> saveFile(model));

        Components.frame = new JFrame("FileReadWrite");
        Components.frame.setSize(300, 400);
        Components.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

    private static void chooseAndLoad(DefaultListModel<PlaceholderItem> model) {
        JFileChooser chooser = new JFileChooser(getFilesDirectory().toFile());
        chooser.setCurrentDirectory(getFilesDirectory().toFile());
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            loadFile(model, chooser.getSelectedFile().toPath());
        }
    }

    private static void loadFile(DefaultListModel<PlaceholderItem> model, Path path) {
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
            List<PlaceholderItem> items = (List<PlaceholderItem>) in.readObject();
            model.clear();
            for (PlaceholderItem item : items) {
                model.addElement(item);
            }
            currentFile = path;
            Components.selectedFile.setText(currentFile.getFileName().toString());
        } catch (IOException | ClassNotFoundException | ClassCastException ex) {
            JOptionPane.showMessageDialog(null, "Could not load file: " + ex.getMessage());
        }


    }

    private static void saveFile(DefaultListModel<PlaceholderItem> model) {
        Path path = currentFile;

        JFileChooser chooser = new JFileChooser(getFilesDirectory().toFile());
        chooser.setCurrentDirectory(getFilesDirectory().toFile());
        if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        path = chooser.getSelectedFile().toPath();


        List<PlaceholderItem> items = new ArrayList<>(Collections.list(model.elements()));
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(path))) {
            out.writeObject(items);
            currentFile = path;
            JOptionPane.showMessageDialog(null, "Saved to " + path.getFileName());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Could not save file: " + ex.getMessage());
        }
        Components.selectedFile.setText(currentFile.getFileName().toString());
    }

    public static void setUpObjectMakePanel(){
        Components.objectPanel = new JPanel(new GridLayout(3, 2));
        Components.objectName = new JLabel();

        Components.nameField = new JTextField(40);

        Components.commitToList = new JButton("Add this Object to file");
        Components.removeObject = new JButton("Delete This objects data in the file");

        Components.commitToList.addActionListener( e -> {
            model.addElement(createObject());
        });

        Components.removeObject.addActionListener( e -> {
            model.removeElement(selected);
        });


        Components.objectPanel.add(Components.objectName);
        Components.objectPanel.add(Components.nameField);
        Components.objectPanel.add(Components.commitToList);
        Components.objectPanel.add(Components.removeObject);
        Components.mainPanel.add(Components.objectPanel);

    }

    public static void RefreshObjList(){
        if (currentFile != null) {
            loadFile(model, currentFile);
        }
    }

    public static PlaceholderItem createObject(){
        return new PlaceholderItem(Components.nameField.getText());
    }

}