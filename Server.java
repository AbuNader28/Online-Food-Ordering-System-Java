import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 12346;
    public static void main(String[] args) {
        // 1. بنشغل الـ MenuManager عشان يقرأ ملف الـ csv مرة واحدة بس في البداية
        MenuManager menuManager = new MenuManager();
        menuManager.loadMenuFromFile("menu.csv");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on port " + PORT + "...");
            System.out.println("Waiting for clients to connect...");
            // 3. حلقة لا نهائية عشان السيرفر يفضل شغال دايماً
            while (true) {
                // البرنامج هيقف هنا لحد ما Client يتصل
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected from: " + clientSocket.getInetAddress());
                // 4. Multithreading: ندي العميل ده لـ Thread منفصل عشان السيرفر يفضى لغيره
                ClientHandler clientHandler = new ClientHandler(clientSocket, menuManager);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Server Error: " + e.getMessage());
        }
    }
}