import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

class ShoppingCartFrame extends JFrame {
    static DefaultTableModel cartTableModel;
    static JTable cartTable;
    private static JLabel totalLabel;
    private static JLabel firstPurchaseDiscountLabel;
    private static JLabel categoryDiscountLabel;
    private static JLabel finalTotalLabel;
    static ShoppingCart cart;
    static User customer;

    private static JButton checkoutButton;

    public ShoppingCartFrame(ShoppingCart cart, User customer) {
        this.cart = cart;
        this.customer = customer;
        setTitle("Shopping Cart");
        setLayout(new GridLayout(2, 1)); // GridLayout with 2 rows and 1 column

        // Top Layer: Table
        JPanel topPanel = new JPanel(new BorderLayout());
        String[] columnNames = {"Product", "Quantity", "Price"};
        cartTableModel = new DefaultTableModel(null, columnNames);
        cartTable = new JTable(cartTableModel);
        cartTable.setRowHeight(80);
        JScrollPane tableScrollPane = new JScrollPane(cartTable);
        tableScrollPane.setPreferredSize(new Dimension(600, 200)); // Adjust the size as needed
        topPanel.add(tableScrollPane, BorderLayout.CENTER);
        add(topPanel);

        // Bottom Layer: Details
        JPanel bottomPanel = new JPanel(new GridLayout(5, 1));
        totalLabel = new JLabel("Total: $" + calculateTotalPrice() );
        firstPurchaseDiscountLabel = new JLabel("First Purchase Discount (10%): -$" + (calculateTotalPrice() * 0.1));
        categoryDiscountLabel = new JLabel("3 items in the same category discount (20%): -$" + (calculateTotalPrice() * 0.2));
        finalTotalLabel = new JLabel("Final Total: $" + calculateTotalPrice());


        //CHECKOUT BUTTON
        checkoutButton = new JButton("Checkout"); // New button
        checkoutButton.addActionListener(e -> {
            // Display a message dialog with JLabel details
            String message = totalLabel.getText() + "\n" +
                    firstPurchaseDiscountLabel.getText() + "\n" +
                    categoryDiscountLabel.getText() + "\n" +
                    finalTotalLabel.getText();

            JOptionPane.showMessageDialog(ShoppingCartFrame.this, message, "Checkout Details", JOptionPane.INFORMATION_MESSAGE);
            customer.incrementPurchases();      //Incrementing purchase history after checkout
            System.out.println(customer.getPurchases());        //Debugging, prints the purchase history
            cart.clearCart();
            updateLabels();     //Updating final label
            //Remove everything from the cart table
            cartTableModel.setRowCount(0);
        });


        bottomPanel.add(totalLabel);
        bottomPanel.add(firstPurchaseDiscountLabel);
        bottomPanel.add(categoryDiscountLabel);
        bottomPanel.add(finalTotalLabel);
        bottomPanel.add(checkoutButton);

        add(bottomPanel);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.initializeShoppingCart();

        pack();
        setLocationRelativeTo(null);
        setSize(600, 500);
    }

    public static void updateLabels() {
        double total = calculateTotalPrice();
        double firstPurchaseDiscount = calculateFirstPurchaseDiscount();
        double categoryDiscount = calculateSameCategoryDiscount();
        double finalTotal = calculateFinalTotal(total, firstPurchaseDiscount, categoryDiscount);

        totalLabel.setText("Total: $" + String.format("%.2f", total));
        firstPurchaseDiscountLabel.setText("First Purchase Discount (10%): -$" + String.format("%.2f", firstPurchaseDiscount));
        categoryDiscountLabel.setText("3 items in the same category discount (20%): -$" + String.format("%.2f", categoryDiscount));
        finalTotalLabel.setText("Final Total: $" + String.format("%.2f", finalTotal));
    }

    private static double calculateTotalPrice() {
        // Logic to calculate total price from the shopping cart data
        double total = 0.0;
        if (cart.getProductList().size() == 0) {
            return 0.0;
        }else {
            for (int i = 0; i < cartTableModel.getRowCount(); i++) {
                total += Double.parseDouble(cartTableModel.getValueAt(i, 2).toString());
            }
        }
        return total;
    }


    private static double calculateFirstPurchaseDiscount() {
        // Assume purchaseCount is a variable to keep track of the number of purchases made by the user
        if (customer.getPurchases() == 0) {
            // First purchase discount (10%)
            return 0.1 * calculateTotalPrice();
        } else {
            return 0.0;
        }
    }

    private static double calculateSameCategoryDiscount() {
        double total = calculateTotalPrice();
        double totalDiscount = 0.0;

        // Assuming there are two categories: "Electronics" and "Clothing"
        int electronicsCount = 0;
        int clothingCount = 0;


        //Use the ShoppingCart Cart in the display class to get the category count
        for (int i = 0; i < cart.getProductList().size(); i++) {
            String category = cart.getProductList().get(i).getCategory();
            System.out.println(category);

            // Update category count
            if ("Electronics".equals(category)) {
                electronicsCount ++;
            } else if ("Clothing".equals(category)) {
                clothingCount ++;
            }
        }


        // Check if there are at least three items of the same category in the cart
        if (electronicsCount >= 3) {
            totalDiscount += 0.2 * total;
        }else if (clothingCount >= 3) {
            totalDiscount += 0.2 * total;
        } else {
            totalDiscount += 0.0;
        }

        return totalDiscount;
    }



    private static double calculateFinalTotal(double total, double firstPurchaseDiscount, double categoryDiscount) {
        // Logic to calculate the final total
        return total - firstPurchaseDiscount - categoryDiscount;
    }
}