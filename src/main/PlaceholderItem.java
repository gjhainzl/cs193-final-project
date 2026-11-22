import java.io.Serializable;

public class PlaceholderItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    public PlaceholderItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name; // this is what the JList displays
    }
}
