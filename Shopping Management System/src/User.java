public class User {
    private String username;
    private String password;
    private int purchases = 0;

    public User(String username, String password, int purchases) {
        this.username = username;
        this.password = password;
        this.purchases = purchases;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void incrementPurchases() {
        purchases++;
    }

    public int getPurchases() {
        return purchases;
    }
}













