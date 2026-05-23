public class Food extends MenuItem {
    private String cuisineType;
    public Food(String id, String name, double price, String cuisineType) {
        super(id, name, price);
        this.cuisineType = cuisineType;
    }
    @Override
    public String getDetails() {
        return "Food: " + getName() + " (" + cuisineType + ") - $" + getPrice();
    }
}
