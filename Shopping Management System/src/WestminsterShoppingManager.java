import java.io.*;
import java.util.*;

public class WestminsterShoppingManager implements ShoppingManager {

    private static final int maxProduct = 50;
    private static ArrayList<Product> productArrayList = new ArrayList<>();
    private static ArrayList<User> userArrayList = new ArrayList<>();

    public static void main(String[] args) {

        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        Scanner input = new Scanner(System.in);

        shoppingManager.loadProduct();

        while (true) {
            try {
                Thread.sleep(1500); // Wait for 1.5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            displayMenu();

            // Get user choice
            try {
                System.out.print("ENTER CHOICE: ");
                int choice = input.nextInt();
                input.nextLine();

                switch (choice) {
                    case 1:
                        addProductInfo();
                        break;
                    case 2:
                        System.out.print("Enter productID: ");
                        String productIDToRemove = input.nextLine();

                        // Find the Product object with the specified ID
                        Product productToRemove = null;
                        for (Product product : productArrayList) {
                            if (product.getProductID().equals(productIDToRemove)) {
                                productToRemove = product;
                                break;
                            }
                        }
                        shoppingManager.removeProduct(productToRemove);
                        break;
                    case 3:
                        shoppingManager.printProduct();
                        break;
                    case 4:
                        shoppingManager.saveProduct();
                        break;
                    case 5:
                        shoppingManager.saveProduct();
                        System.out.println("Exiting the program. Thank you!");
                        System.exit(0);
                        break;
                    case 6:
                        //GUI
                        System.out.println("Online shopping cart (GUI)");
                        LoginFrame loginFrame = new LoginFrame();
                        loginFrame.setVisible(true);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 6.\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid integer choice");
                input.nextLine(); // Consume the invalid input to avoid an infinite loop
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("An unexpected error occurred");
                input.nextLine();
            }
        }
    }


    // Getters for product and user lists
    public static ArrayList<Product> getProductArrayList() {
        return productArrayList;
    }
    public static ArrayList<User> getUserArrayList() {
        return userArrayList;
    }


    public static void displayMenu() {
        System.out.println("<<<------------------------------------>>>");
        System.out.println("WELCOME TO WESTMINSTER SHOPPING");
        System.out.println("1. Add a product");
        System.out.println("2. Delete a product");
        System.out.println("3. Print product list");
        System.out.println("4. Save in a file");
        System.out.println("5. EXIT");
        System.out.println("6. GUI");
        System.out.println("<<<------------------------------------>>>");
    }

    public void addProduct(Product product) {
        try {
            if (productArrayList.size() < maxProduct) {
                productArrayList.add(product);
                System.out.println("Product added successfully\n");
                Display.addProductToTable(getProductDetails(product));


            } else {
                System.out.println("You have reached the maximum limit\n");
            }
        } catch (Exception e) {
            System.out.println("Could not add product. Please try again.\n");
        }
    }

    public static void addProductInfo() {
        int productType;
        Scanner input = new Scanner(System.in);
        WestminsterShoppingManager shoppingManager = new WestminsterShoppingManager();
        while (true) {
            try {
                System.out.print("Enter product type (1 for Electronics, 2 for Clothing): ");
                productType = input.nextInt();
                input.nextLine();

                if (productType == 1 || productType == 2) {
                    break; // Exit the loop if the input is valid
                } else {
                    System.out.println("Invalid product type. Please enter 1 for Electronics or 2 for Clothing.\n");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.\n");
                input.nextLine(); // Clear the invalid input from the scanner
            }
        }
        System.out.print("Enter product ID: ");
        String productID = input.nextLine();

        // Validate if the productID already exists
        while (shoppingManager.isProductIDExists(productID)) {
            System.out.println("Product with the same ID already exists. Please enter a different product ID.\n");
            System.out.print("Enter product ID: ");
            productID = input.nextLine();
        }

        System.out.print("Enter product name: ");
        String productName = input.nextLine();

        int availableItems;
        while (true) {
            try {
                System.out.print("Enter available items: ");
                availableItems = input.nextInt();
                input.nextLine(); // Consume newline
                if (availableItems >= 0) {
                    break; // Exit the loop if the input is valid
                } else {
                    System.out.println("Invalid input. Please enter a non-negative number for available items.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input.nextLine(); // Clear the invalid input from the scanner
            }
        }

        double price;
        while (true) {
            try {
                System.out.print("Enter price: ");
                price = input.nextDouble();
                input.nextLine(); // Consume newline

                if (price >= 0) {
                    break; // Exit the loop if the input is valid
                } else {
                    System.out.println("Invalid input. Please enter a non-negative number for the price.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input.nextLine(); // Clear the invalid input from the scanner
            }
        }

        try{
            if (productType == 1) {
                // For Electronics
                System.out.print("Enter brand: ");
                String brand = input.nextLine();

                int warrantyPeriod;
                while (true) {
                    try {
                        System.out.print("Enter warranty period (In months): ");
                        warrantyPeriod = input.nextInt();
                        input.nextLine(); // Consume newline

                        if (warrantyPeriod >= 0) {
                            break; // Exit the loop if the input is valid
                        } else {
                            System.out.println("Invalid input. Please enter a non-negative number for the warranty period.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        input.nextLine(); // Clear the invalid input from the scanner
                    }
                }

                // Create a new Electronics object
                Electronics electronics = new Electronics(productID, productName, "Electronics", availableItems, price, brand, warrantyPeriod);
                shoppingManager.addProduct(electronics);

            } else {
                // For Clothing
                System.out.print("Enter size (S, M, L, XL): ");
                String size = input.nextLine().toUpperCase(); // Convert to uppercase for case-insensitive comparison
                while (!(size.equals("S") || size.equals("M") || size.equals("L") || size.equals("XL"))) {
                    System.out.println("Invalid size. Please enter S, M, L, or XL.");
                    System.out.print("Enter size (S, M, L, XL): ");
                    size = input.nextLine().toUpperCase();
                }

                System.out.print("Enter color: ");
                String color = input.nextLine();

                // Create a new Clothing object
                Clothing clothing = new Clothing(productID, productName, "Clothing",availableItems, price, size, color);

                // Add the product to the shopping manager
                shoppingManager.addProduct(clothing);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Invalid input. Please try again.");
        }

    }

    // Method to check if a product with the given productID already exists
    public boolean isProductIDExists(String productID) {
        for (Product product : productArrayList) {
            if (product.getProductID().equals(productID)) {
                return true;
            }
        }
        return false;
    }


    //GUI DISPLAYING TEXT
    public String getProductDetails(Product product) {
        if (product.getCategory().equals("Electronics")) {
            return product.getProductID() + "\t" + product.getProductName() + "\t" + product.getCategory() + "\t" +
                    product.getPrice() + "\t" + product.getProductInfo();
        } else if (product.getCategory().equals("Clothing")) {
            return product.getProductID() + "\t" + product.getProductName() + "\t" + product.getCategory() + "\t"+
                    product.getPrice() + "\t" + product.getProductInfo();
        } else {
            return ""; // Handle other product types if needed
        }
    }


    public void removeProduct(Product product) {
        if (productArrayList.contains(product)) {
            String productIDToRemove = product.getProductID();
            productArrayList.remove(product);

            // Remove the product details from the GUI table
            Display.removeProductFromTable(productIDToRemove);
            System.out.println("Product with ID '" + productIDToRemove + "' has been removed.");
            System.out.println("Product removed successfully");
        } else {
            System.out.println("Product not found in the list");
        }
    }


    public void printProduct() {
        if (productArrayList.isEmpty()) {
            System.out.println("No products to display.");
            return;
        }

        // Sort the products by product ID before printing
        productArrayList.sort(Comparator.comparing(Product::getProductID));

        System.out.println("Product List:");
        for (Product product : productArrayList) {
            System.out.println(product);
        }
    }

    public void saveProduct() {
        try {
            FileWriter myWriter = new FileWriter("data.txt");

            for (Product product : productArrayList) {
                if (product instanceof Electronics electronics) {
                    myWriter.write("Electronics\t" + electronics.getProductID() + "\t" + electronics.getProductName()
                            + "\t" + electronics.getAvailableItems() + "\t" + electronics.getPrice() + "\t"
                            + electronics.getBrand() + "\t" + electronics.getWarrantyPeriod() + "\n");
                } else if (product instanceof Clothing clothing) {
                    myWriter.write("Clothing\t" + clothing.getProductID() + "\t" + clothing.getProductName() + "\t"
                            + clothing.getAvailableItems() + "\t" + clothing.getPrice() + "\t" + clothing.getSize() + "\t"
                            + clothing.getColour() + "\n");
                }
            }
            saveUser();
            myWriter.close();
            System.out.println("Successfully saved to the file.");
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    // Method to load product information from a file
    public void loadProduct() {
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.split("\t");
                if (arr.length >= 7) {
                    String productType = arr[0];
                    String productID = arr[1];

                    boolean productExists = isProductIDExists(productID);

                    if (!productExists) {
                        String productName = arr[2];
                        int availableItems = Integer.parseInt(arr[3]);
                        double price = Double.parseDouble(arr[4]);

                        if (productType.equals("Clothing")) {
                            String size = arr[5];
                            String color = arr[6];
                            Clothing clothing = new Clothing(productID, productName, "Clothing",availableItems, price, size, color);
                            productArrayList.add(clothing);
                        } else if (productType.equals("Electronics")) {
                            String brand = arr[5];
                            int warrantyPeriod = Integer.parseInt(arr[6]);
                            Electronics electronics = new Electronics(productID, productName, "Electronics",availableItems, price, brand, warrantyPeriod);
                            productArrayList.add(electronics);
                        }
                    }
                }
            }
            loadUser();
            System.out.println("Product Load successfully.");
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    //Method to store user data
    public void saveUser() {
        try {
            FileWriter myWriter = new FileWriter("users.txt");

            for (User user : userArrayList) {
                myWriter.write(user.getUsername() + "," + user.getPassword() + "," + user.getPurchases() + "\n");
            }
            myWriter.close();
            System.out.println("\nSuccessfully Users saved to the file.");
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    //Make a method to load user data
    public void loadUser() {
        try (BufferedReader br = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] arr = line.split(",");
                if (arr.length >= 3) {
                    String username = arr[0];
                    String password = arr[1];
                    int purchases = Integer.parseInt(arr[2]);

                    boolean userExists = isUsernameExists(username);

                    if (!userExists) {
                        User user = new User(username, password, purchases);
                        userArrayList.add(user);
                    }
                }
            }
            System.out.println("User Load successfully.");
        } catch (IOException ex) {
            System.out.println("An error occurred: " + ex.getMessage());
        }
    }

    //Method to check if username already exists
    public boolean isUsernameExists(String username) {
        for (User user : userArrayList) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }
}



