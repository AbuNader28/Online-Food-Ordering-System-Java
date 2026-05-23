import java.io.Serializable;

// بنستخدم Serializable عشان نبعت الأوبجكتس دي بين الـ Client والـ Server عبر الـ Sockets
public abstract class MenuItem implements Serializable {
    private String id;
    private String name;
    private double price;
    public MenuItem(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public String getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public abstract String getDetails();
}