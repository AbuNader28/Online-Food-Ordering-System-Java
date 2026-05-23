import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12346;

    public static void main(String[] args) {
        // بنفتح الاتصال مع السيرفر وبنجهز أدوات الإرسال والاستقبال
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Connected to the Food Ordering Server!");
            // 1. نستقبل المنيو من السيرفر ونطبعها للعميل
            List<MenuItem> menu = (List<MenuItem>) in.readObject();
            System.out.println("\n--- 🍔 Menu 🍕 ---");
            for (MenuItem item : menu) {
                // هنا بنستخدم الـ Polymorphism لأن دالة getDetails بتشتغل حسب نوع الكلاس
                System.out.println(item.getId() + " - " + item.getDetails());
            }
            System.out.println("------------------\n");
            // 2. نطلب من العميل يكتب طلبه
            System.out.print("Enter the IDs of the items you want to order (e.g., F01, F03): ");
            String orderRequest = scanner.nextLine();
            // 3. نبعت الطلب للسيرفر
            out.writeObject(orderRequest);
            out.flush();
            // 4. نستقبل رسالة التأكيد من السيرفر ونطبعها
            String serverResponse = (String) in.readObject();
            System.out.println("\n" + serverResponse);
        } catch (Exception e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }
}