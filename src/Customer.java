/**
 * Represents an individual Customer who will open
 * their own account within the ATM system
 *
 */

public class Customer {

    private final String name;
    private String PIN;

    Account savings;
    Account checking;

    public Customer(String name, String PIN) {
        this.name = name;
        this.PIN = PIN;
        savings = new Account(false);
        checking = new Account(true);
    }

    /**
     * Returns the customer's name
     *
     * @return customer's name
     * */
    public String getName() {return name;}

    /**
     * Returns the Customer's saving account
     *
     * @return The savings object
     */
    public Account getSavings() {return savings;}

    /**
     * Returns the Customer's checking account
     *
     * @return The checking object
     */
    public Account getChecking() {return checking;}

    /**
     * Changes the PIN associated with the Customer
     *
     * @param newPIN The new PIN that the Customer will input
     */
    public void setPIN(String newPIN) {PIN = newPIN;}

    /**
     * Compares the PIN and the input
     *
     * @param input The string that will be compared to the PIN
     * @return true if the PIN and string input are the same; false if otherwise
     */
    public boolean confirmPIN(String input) {return PIN.equals(input);}
}
