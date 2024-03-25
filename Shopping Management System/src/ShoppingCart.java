import java.util.ArrayList;

public class ShoppingCart {
    private static ArrayList<Product> productList = new ArrayList<>();
    private static User currentUser;

    public static void addProduct(Product product) {
        productList.add(product);
    }

    public static ArrayList<Product> getProductList() {
        return productList;
    }

    public static void removeProduct(Product product) {
        productList.remove(product);
    }


    //Method for logged-in or registered user
    public void initializeShoppingCart() {
        User currentUser = LoginFrame.getRegisteredUser();
        if (currentUser != null) {
            ShoppingCart.setCurrentUser(currentUser);
        }
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void clearCart() {
        productList.clear();
    }
}
