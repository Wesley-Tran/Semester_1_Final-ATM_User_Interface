/**
 * Represents an individual Customer who will open
 * their own account within the ATM system
 *
 */

public class Customer {

    private final String name;
    private int PIN;

    Account savings;
    Account checking;

    public Customer(String name, int PIN) {
        this.name = name;
        this.PIN = PIN;
        savings = new Account(false);
        checking = new Account(true);
    }

    /**returns the customer's name
     * @return customer's name*/
    public String getName() {return name;}

    public Account getSavings() {return savings;}

    public Account getChecking() {return checking;}

    public void setPIN(int newPIN) {PIN = newPIN;}
    public boolean confirmPIN(int input) {return PIN == input;}
}
