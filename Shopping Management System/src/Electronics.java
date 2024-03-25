public class Electronics extends Product {
    private String brand;
    private int warrantyPeriod;


    public Electronics(String productID, String productName, String Category, int availableItems, double price, String brand, int warrantyPeriod) {
        super(productID, productName, Category, availableItems, price);
        this.brand = brand;
        this.warrantyPeriod = warrantyPeriod;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(int warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }

    @Override
    public String getProductInfo() {
        return brand + ", " + warrantyPeriod + " weeks warranty";
    }

    @Override
    public String toString() {
        return "Electronics\n" +
                super.toString() +
                "\nBrand: " + brand +
                "\nWarranty Period: " + warrantyPeriod + "\n";
    }
}




