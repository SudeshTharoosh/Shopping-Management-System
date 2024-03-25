import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private static ArrayList<User> userList = WestminsterShoppingManager.getUserArrayList();
    private static User registeredUser;  // Add this variable

    public LoginFrame() {
        setTitle("Login");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel usernameLabel = new JLabel("Username:");
        add(usernameLabel);

        usernameField = new JTextField(20);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        add(passwordLabel);

        passwordField = new JPasswordField(20);
        add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (validateUser(username, password)) {
                dispose();
                User user = getRegisteredUser();
                Display display = new Display(user);
                display.initializeProducts();
                display.setVisible(true);

                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.initializeShoppingCart();
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(loginButton);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            dispose();
            RegisterFrame registerFrame = new RegisterFrame();
            registerFrame.setVisible(true);
        });
        add(registerButton);

        pack();
        loadUsers();
        setLocationRelativeTo(null);
    }

    public boolean validateUser(String username, String password) {
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                registeredUser = user;  // Set the logged-in user
                return true;
            }
        }
        return false;
    }



    public static void addUser(User user) {
        userList.add(user);
    }

    public void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    String purchases = parts[2];
                    int purchasesInt = Integer.parseInt(purchases);
                    userList.add(new User(username, password, purchasesInt));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("User file not found. Creating a new one.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static User getRegisteredUser() {
        return registeredUser;
    }
}