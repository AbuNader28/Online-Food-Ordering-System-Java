import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private MenuManager menuManager;
    public ClientHandler(Socket clientSocket, MenuManager menuManager) {
        this.clientSocket = clientSocket;
        this.menuManager = menuManager;
    }
    @Override
    public void run() {
        try (
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            System.out.println("Handling client on thread: " + Thread.currentThread().getName());
            // 1. نبعت المنيو للعميل
            List<MenuItem> menu = menuManager.getMenu();
            out.writeObject(menu);
            out.flush();
            // 2. نستقبل الطلب من العميل (العميل هيبعت الأكواد مفصولة بفاصلة، مثلا: F01, F09)
            String orderRequest = (String) in.readObject();
            System.out.println("Received order request from client: " + orderRequest);
            // 3. معالجة الطلب وحساب الفاتورة
            String[] requestedIds = orderRequest.split(",");
            double totalBill = 0.0;
            StringBuilder responseMessage = new StringBuilder();
            boolean hasValidItems = false;
            responseMessage.append("--- Your Order Receipt ---\n");
            // نلف على الأكواد اللي العميل طلبها
            for (String reqId : requestedIds) {
                String cleanId = reqId.trim().toUpperCase(); // تنظيف المسافات وتحويل الحروف لـ Capital
                boolean itemFound = false;
                // ندور على الكود ده جوه المنيو
                for (MenuItem item : menu) {
                    if (item.getId().equals(cleanId)) {
                        totalBill += item.getPrice();
                        responseMessage.append("Added: ").append(item.getName())
                                .append(" ($").append(item.getPrice()).append(")\n");
                        itemFound = true;
                        hasValidItems = true;
                        break;
                    }
                }
                // لو لفينا على المنيو كلها ومالقيناش الكود
                if (!itemFound) {
                    responseMessage.append("Warning: Item ID '").append(cleanId).append("' is NOT found in our menu.\n");
                }
            }
            // 4. تجهيز الرد النهائي
            if (hasValidItems) {
                responseMessage.append("--------------------------\n");
                responseMessage.append("Total Bill: $").append(totalBill).append("\n");
                responseMessage.append("Thank you for your order! It is being prepared.");
            } else {
                responseMessage.append("Order Failed: You did not enter any valid item IDs.");
            }
            // 5. نبعت النتيجة (الفاتورة أو رسالة الخطأ) للعميل
            out.writeObject(responseMessage.toString());
            out.flush();
        } catch (Exception e) {
            System.err.println("Client disconnected or an error occurred: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}