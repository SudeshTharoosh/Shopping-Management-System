public class Clothing extends Product {
    private String size;
    private String colour;

    public Clothing(String productID, String productName, String Category, int availableItems, double price, String size, String colour) {
        super(productID, productName, Category, availableItems, price);
        this.size = size;
        this.colour = colour;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @Override
    public String getProductInfo() {
        return size + ", " + colour;
    }

    @Override
    public String toString() {
        return "Clothing\n" +
                super.toString() +
                "\nSize: " + size +
                "\nColour: " + colour + "\n";
    }
}
