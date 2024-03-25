import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

class Display extends JFrame {
    static JTable productTable;
    private static DefaultTableModel tableModel;
    private static JLabel detailsLabel;
    private WestminsterShoppingManager shoppingManager;

    private ShoppingCartFrame shoppingCartFrame;  // Remove static reference
    public ShoppingCart Cart;

    public Display(User user) {
        tableModel = new DefaultTableModel();

        Cart.clearCart();
        User customer = user;
        this.shoppingManager = new WestminsterShoppingManager();
        shoppingCartFrame = new ShoppingCartFrame(Cart, customer);
        //When exit save the products to the file
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                shoppingManager.saveProduct();
                System.exit(0);
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2,1));
        add(topPanel,BorderLayout.NORTH);

        JPanel topInfoPanel = new JPanel();
        topInfoPanel.setLayout(new GridLayout(1,3));
        topPanel.add(topInfoPanel,BorderLayout.NORTH);



        JPanel categoryPanel = new JPanel(new FlowLayout());
        topInfoPanel.add(categoryPanel,BorderLayout.WEST);

        JLabel instructions = new JLabel("Select Product Category");
        categoryPanel.add(instructions,BorderLayout.WEST);


        JPanel categoryBoxPanel = new JPanel(new FlowLayout());
        topInfoPanel.add(categoryBoxPanel,BorderLayout.EAST);

        String[] categories = {"All", "Electronics", "Clothing"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        categoryBoxPanel.add(categoryComboBox, BorderLayout.EAST);

        categoryComboBox.addActionListener(e -> {
            String selectedCategory = (String) categoryComboBox.getSelectedItem();
            filterProductsByCategory(selectedCategory);
        });


        JPanel shoppingCartButtonPanel = new JPanel(new FlowLayout());
        topInfoPanel.add(shoppingCartButtonPanel,BorderLayout.CENTER);


        JButton shoppingCartButton = new JButton("Shopping Cart");
        shoppingCartButton.addActionListener(e -> shoppingCartFrame.setVisible(true));
        shoppingCartButtonPanel.add(shoppingCartButton,BorderLayout.CENTER);


        //TABLE
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2,1));
        add(bottomPanel,BorderLayout.SOUTH);


        String[] columnNames = {"ProductID", "Name", "Category", "Price", "Info"};
        tableModel = new DefaultTableModel(null, columnNames);
        productTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tableScrollPane.setPreferredSize(new Dimension(400, 100));
        add(tableScrollPane);

        // Add a ListSelectionListener to the productTable
        detailsLabel = new JLabel();
        bottomPanel.add(detailsLabel);

        //Display the content of the table below when user clicks
        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow != -1) {
                    displaySelectedProductDetails(selectedRow);
                }
            }
        });


        JButton addToCartButton = new JButton("Add to Shopping Cart");
        addToCartButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                addToShoppingCart(selectedRow);
            } else {
                JOptionPane.showMessageDialog(Display.this, "Please select a product to add to the shopping cart.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottomBtnPanel = new JPanel();
        bottomPanel.add(bottomBtnPanel,BorderLayout.SOUTH);
        bottomBtnPanel.add(addToCartButton,BorderLayout.SOUTH);

        setTitle("Westminster Shopping Center");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void initializeProducts() {
        displayAllProducts();
    }

    public void filterProductsByCategory(String selectedCategory) {
        if ("All".equals(selectedCategory)) {
            displayAllProducts();
        } else if ("Electronics".equals(selectedCategory)) {
            displayElectronicsProducts();
        } else if ("Clothing".equals(selectedCategory)) {
            displayClothingProducts();
        }
    }


    //DISPLAY ALL PRODUCTS ON THE TABLE
    public void displayAllProducts() {
        ArrayList<Product> productArrayList = WestminsterShoppingManager.getProductArrayList();
        // Sort the productList using a custom comparator
        productArrayList.sort(Comparator.comparing(Product::getProductID));

        tableModel.setRowCount(0);
        for (Product product : WestminsterShoppingManager.getProductArrayList()) {
            addProductToTable(shoppingManager.getProductDetails(product));
        }
    }



    //FILTERING ELECTRONICS
    public void displayElectronicsProducts() {
        tableModel.setRowCount(0);
        for (Product product : WestminsterShoppingManager.getProductArrayList()) {
            if (product.getCategory().equals("Electronics")) {
                addProductToTable(shoppingManager.getProductDetails(product));
            }
        }
    }

    //FILTERING CLOTHING
    public void displayClothingProducts() {
        tableModel.setRowCount(0);
        for (Product product : WestminsterShoppingManager.getProductArrayList()) {
            if (product.getCategory().equals("Clothing")) {
                addProductToTable(shoppingManager.getProductDetails(product));
            }
        }
    }


    //Display selected product on the display frame
    public void displaySelectedProductDetails(int selectedRow) {
        StringBuilder details = new StringBuilder("<html><body>Selected Product - Details<br><br>");

        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            String columnName = tableModel.getColumnName(i);
            String value = tableModel.getValueAt(selectedRow, i).toString();

            if ("Price".equals(columnName)) {
                // Skip the Price column
                continue;
            }

            if ("Info".equals(columnName)) {
                // Handle attributes differently based on the category
                String category = tableModel.getValueAt(selectedRow, 2).toString(); // Assuming category is at index 2

                if ("Clothing".equals(category)) {
                    // For Clothing, split the Info column into Size and Color
                    String[] infoAttributes = value.split(", ");
                    details.append("<b>Size:</b> ").append(infoAttributes[0]).append("<br>");
                    details.append("<b>Color:</b> ").append(infoAttributes[1]).append("<br>");
                } else if ("Electronics".equals(category)) {
                    // For Electronics, split the Info column into Brand and Warranty
                    String[] infoAttributes = value.split(", ");
                    details.append("<b>Brand:</b> ").append(infoAttributes[0]).append("<br>");
                    details.append("<b>Warranty:</b> ").append(infoAttributes[1]).append("<br>");
                }
            } else {
                details.append("<b>").append(columnName).append(":</b> ").append(value).append("<br>");
            }
        }


        // Retrieve the selected product based on the row index
        int rowIndex = productTable.convertRowIndexToModel(selectedRow);
        Product selectedProduct = WestminsterShoppingManager.getProductArrayList().get(rowIndex);

        // Assuming "Available Items" is a method in your Product class
        details.append("<b>Available Items:</b> ").append(selectedProduct.getAvailableItems()).append("<br>");


        // NEW //Check availability and set color
        int availableItems = selectedProduct.getAvailableItems();
        if (availableItems < 3) {
            productTable.setSelectionForeground(Color.RED);
        } else {
            productTable.setSelectionForeground(Color.BLACK);
        }

        details.append("</body></html>");
        detailsLabel.setText(details.toString());
    }


    //UPDATING THE TABLE LIVELY WHEN ADDING PRODUCTS
    public static void addProductToTable(String productDetails) {

        String[] rowData;
        rowData = productDetails.split("\t");
        try {
            if (tableModel != null) {
                tableModel.addRow(rowData);
            }
//            } else {
//                System.out.println("TableModel is null. Make sure it's properly initialized.");
//            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Unable to add row to table.");
        }
    }


    //UPDATING THE TABLE LIVELY WHEN REMOVING PRODUCTS

    public static void removeProductFromTable(String productID) {
        if (tableModel != null) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if (tableModel.getValueAt(i, 0).equals(productID)) {
                    tableModel.removeRow(i);
                    break;
                }
            }
        } else {
            System.out.println("Error: Table model is null.");
        }
    }


    public void addToShoppingCart(int selectedRow) {
        int rowIndex = productTable.convertRowIndexToModel(selectedRow);
        Product selectedProduct = WestminsterShoppingManager.getProductArrayList().get(rowIndex);

        if (selectedProduct.getAvailableItems() > 0) {
            if (ShoppingCartFrame.cartTableModel != null) {
                int existingRowIndex = findProductInCart(selectedProduct);
                if (existingRowIndex != -1) {
                    updateCartItem(existingRowIndex, selectedRow);
                } else {
                    String[] rowData = getProductTableRow(selectedProduct);
                    ShoppingCartFrame.cartTableModel.addRow(rowData);
                    updateAvailableItems(selectedProduct);
                }
                Cart.addProduct(selectedProduct);
                ShoppingCartFrame.updateLabels();       //Update label every click
                shoppingCartFrame.setVisible(true);
            } else {
                System.err.println("Error: ShoppingCartFrame.cartTableModel is null");
            }
        } else {
            JOptionPane.showMessageDialog(Display.this, "The product is out of stock.", "Out of Stock", JOptionPane.WARNING_MESSAGE);
        }
    }



    // Method to update the available items and display a message
    public void updateAvailableItems(Product product) {
        int availableItems = product.getAvailableItems();

        // Check if available items reach the maximum limit
        if (availableItems == 0) {
            JOptionPane.showMessageDialog(Display.this, "The product is out of stock.", "Out of Stock", JOptionPane.WARNING_MESSAGE);
        } else {
            // Decrement the available items
            product.setAvailableItems(availableItems - 1);
            displaySelectedProductDetails(productTable.getSelectedRow());
        }
    }


    public int findProductInCart(Product product) {
        for (int i = 0; i < ShoppingCartFrame.cartTableModel.getRowCount(); i++) {
            String cartProductDetails = (String) ShoppingCartFrame.cartTableModel.getValueAt(i, 0);

            // Assuming product details are represented as a string in the cart
            if (cartProductDetails.equals(getProductTableRow(product)[0])) {
                return i;
            }
        }
        return -1;
    }


    // Method to get a row of product details for the shopping cart
    public String[] getProductTableRow(Product product) {
        String[] rowData = new String[3];
        String productDetails = formatProductDetails(product);
        rowData[0] = productDetails;
        rowData[1] = "1"; // Initial quantity is 1 for a new product
        rowData[2] = String.valueOf(product.getPrice());
        return rowData;
    }


    // Method to format product details based on the category displayed on the shopping cart frame
    public String formatProductDetails(Product product) {
        StringBuilder details = new StringBuilder("<html><body>");

        details.append(product.getProductID()).append("<br>");
        details.append(product.getProductName()).append("<br>");

        if (product instanceof Clothing clothing) {
            details.append(clothing.getSize()).append(",").append(clothing.getColour()).append("<br>");
        } else if (product instanceof Electronics electronics) {
            details.append(electronics.getBrand()).append(",").append(electronics.getWarrantyPeriod()).append(" months warranty").append("<br>");
        } else {
            // Add handling for other categories if needed
            details.append(product.getProductID()).append("<br>");
            details.append(product.getProductName()).append("<br>");
        }
        details.append("</body></html>");
        return details.toString();
    }


    // Method to increment quantity and update price in the shopping cart
    public void updateCartItem(int rowIndex, int selectedRow) {
        // Retrieve the current quantity from the cart table
        String quantityAsString = (String) ShoppingCartFrame.cartTableModel.getValueAt(rowIndex, 1);

        try {
            // Convert String values to appropriate numeric types
            int quantity = Integer.parseInt(quantityAsString);

            // Check if the product is still in stock
            int rowIndexInProductTable = productTable.convertRowIndexToModel(selectedRow);
            Product product = WestminsterShoppingManager.getProductArrayList().get(rowIndexInProductTable);

            // Check if the available items are sufficient
            if (product.getAvailableItems() > 0) {
                // Increment quantity by 1
                quantity++;

                // Calculate the new price based on the updated quantity
                double newPrice = quantity * product.getPrice();

                // Update the cart table with the new quantity and price
                ShoppingCartFrame.cartTableModel.setValueAt(String.valueOf(quantity), rowIndex, 1);
                ShoppingCartFrame.cartTableModel.setValueAt(String.format("%.2f", newPrice), rowIndex, 2);

                // Update the available items and details labels
                updateAvailableItems(product);
            } else {
                JOptionPane.showMessageDialog(Display.this, "The product is out of stock.", "Out of Stock", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException | ClassCastException e) {
            e.printStackTrace();
        }
    }
}