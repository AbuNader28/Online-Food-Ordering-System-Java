import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuManager {
    private List<MenuItem> menuList;
    public MenuManager() {
        menuList = new ArrayList<>();
    }
    public void loadMenuFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    String id = data[0].trim();
                    String name = data[1].trim();
                    double price = Double.parseDouble(data[2].trim());
                    String cuisineType = data[3].trim();
                    Food foodItem = new Food(id, name, price, cuisineType);
                    menuList.add(foodItem);
                }
            }
            System.out.println("Menu loaded successfully! Items found: " + menuList.size());
        } catch (IOException e) {
            System.err.println("Error reading the menu file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error in price format in the file.");
        }
    }
    public List<MenuItem> getMenu() {
        return menuList;
    }
}