public abstract class Product {
    private String productID;
    private String productName;
    private int availableItems;
    private String category;
    private double price;

    public Product(String productID, String productName, String category, int availableItems, double price) {
        this.productID = productID;
        this.productName = productName;
        this.category = category;
        this.availableItems = availableItems;
        this.price = price;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(int availableItems) {
        this.availableItems = availableItems;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory(){
        return category;
    }

    public abstract String getProductInfo();

    @Override
    public String toString() {
        return "Product ID: " + productID +
                "\nName: " + productName +
                "\nQuantity: " + availableItems +
                "\nPrice: $" + price;
    }
}
