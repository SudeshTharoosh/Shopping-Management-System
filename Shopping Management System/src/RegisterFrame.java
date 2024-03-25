import javax.swing.*;
import java.awt.*;
class RegisterFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public RegisterFrame() {
        setTitle("Register");
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel usernameLabel = new JLabel("Username:");
        add(usernameLabel);

        usernameField = new JTextField(20);
        add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        add(passwordLabel);

        passwordField = new JPasswordField(20);
        add(passwordField);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (!username.isEmpty() && !password.isEmpty()) {
                User newUser = new User(username, password, 0);


                LoginFrame.addUser(newUser);
                JOptionPane.showMessageDialog(RegisterFrame.this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                // Dispose of the RegisterFrame
                dispose();

                Display display = new Display(newUser);
                display.setVisible(true);
                display.initializeProducts();

                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.initializeShoppingCart();
            } else {
                JOptionPane.showMessageDialog(RegisterFrame.this, "Username and password cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        add(registerButton);

        pack();
        setLocationRelativeTo(null);
    }
}