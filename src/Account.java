/**
 * The class that creates the accounts for the customer
 * <p>
 * Both checking and savings
 */

public class Account {

    boolean type;
    private double balance;
    public Account(boolean type) {
        this.type = type; //true for checking; false for savings
        balance = 0.0;
    }

    /** Returns the balance */
    public double getBal() {return balance;}
    /** Returns the type of account */
    public boolean getType() {return type;}

    /**
     * Adds the amount of money into the balance
     *
     * @param amount The amount of money that the user will input
     */
    public void deposit(double amount) {balance += amount;}
    /**
     * Subtracts the amount of money into the balance
     *
     * @param amount The amount of money that the user will input
     */
    public void withdraw(double amount) {balance -= amount;}

    /**
     * Transfers the amount of money from account to account
     *
     * @param receiver The account receiving the money
     * @param amount The amount of money that the user is inputting
     * @return Returns true if the transaction was a success and false if it was not
     */
    public boolean transfer(Account receiver, double amount) {
        if (balance >= amount) {
            receiver.deposit(amount);
            withdraw(amount);
            return true;
        }
        return false;

    }

    /**
     * Prints the string when you try to print the Object
     *
     * @return The string of what kind of account it is
     */
    @Override
    public String toString() {
        if (type) {
            return "checking";
        }
        return "savings";
    }



}
