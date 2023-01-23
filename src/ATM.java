/**
 * Uses the other classes and their methods to
 * print out the interface and handle sequencing
 *
 * @author Wesley Tran
 */

import java.util.Scanner;

public class ATM {

    private String printMsg = "";
    private Customer customer = null;
    private int transactionID = 10001;
    /**
     * Only public method and will start the program by prompting for their name and PIN
     */
    public void start() {
        boolean cont = true;
        String name;
        Scanner scan = new Scanner(System.in);

        if (customer == null) {
            System.out.println("Welcome to the ATM\n\nTo get started with this ATM, you need to start an account\n");
            System.out.print("Please enter your name:\n> ");
            name = scan.nextLine();
            customer = new Customer(name, ""); //give the customer a temp PIN and immediately tell them to set it
            choosePIN(scan);
        }
        while (cont) {
            checkPIN(scan);
            printGUI();
            displayMsg();
            int choice = scan.nextInt();
            scan.nextLine();
            processChoice(choice, scan);
            displayMsg(); //display again for the receipt
            if (choice==6) {
                System.out.println("Come again soon!");
                break;
            }
            System.out.print("Would you like to do anything else? (y/n)\n> ");
            String temp = scan.nextLine();
            if (temp.equals("n")) { //program will end
                cont = false;
                displayMsg();
                System.out.println("\n\nCome again soon!");
            }
        }
    }

    /** Prints printMsg and clears the string to be used again*/
    private void displayMsg() {
        System.out.print(printMsg);
        printMsg = "";
    }

    /** Adds the menu to the printMsg string to be printed*/
    private void printGUI() {
        printMsg += "\n\n";
        System.out.print("\033[H\033[2J"); // will not work in intellij's IDE but in terminal, it will clear the screen
        System.out.flush();
        printMsg += "--------------- " + customer.getName() + "'s ATM ---------------\n";
        printMsg += "Please choose an option: \n";
        printMsg += "1. Withdraw money\n";
        printMsg += "2. Deposit money\n";
        printMsg += "3. Transfer money between accounts\n";
        printMsg += "4. Get account balances\n";
        printMsg += "5. Change PIN\n";
        printMsg += "6. Exit\n";
        printMsg += "Please choose the number that corresponds to the menu \n> ";
    }

    private void printReciept(int choice, boolean transaction, double amount, Account account) {
        String msg = "";
        String condition = "";
        if (!transaction) {condition = "failed to";}

        switch(choice) {
            case 1:
                if (!transaction) {msg += "Insufficient funds in that account!\n\n";}
                else {msg += "Withdrew $" + amount + " from " + account + "\nTransaction ID: " + transactionID + "\n\n";}
                break;
            case 2:
                msg += condition + "Deposited $" + amount + " into " + account + "\nTransaction ID: " + transactionID + "\n\n";
                break;
            case 3:
                String acc2 = "checking";
                if (account.getType()) {acc2 = "savings";}
                if (!transaction) {msg += "Insufficient funds in " + account + " account to transfer\n\n";}
                else {msg += "Transfer of $" + amount + " from " + account + " to " + acc2 + " account" +"\nTransaction ID: " + transactionID + "\n\n";}
                break;
            case 4:
                msg += "Your savings account has: $" + customer.getSavings().getBal() + "\n";
                msg += "Your checking account has: $" + customer.getChecking().getBal() + "\n";
                break;
            case 5:
                msg += "Changed PIN: complete\n";
                break;
            case 6:
                break;
        }

        printMsg += "\n" + msg;

    }

    /**
     * Runs a method or does an action based on the choice that the user chose
     *
     * @param choice the option that the user chose and the action that they wanted to make
     * @param scan Scanner object that allows for user input
     */
    private void processChoice(int choice, Scanner scan) {
        if (!(choice > 0 && choice < 7)) {
            System.out.println("\nPlease enter a valid option\n");
            printGUI();
            displayMsg();
            choice = scan.nextInt();
            scan.nextLine();
            processChoice(choice,scan);
        } else {
            double amount;
            switch(choice) {
                case 1:
                    amount = howMuch(scan);
                    if (fromAccount(scan)) { // decides which one to withdraw from and how much
                        if (customer.getChecking().getBal() >= amount) {
                            while (!(amount % 5 == 0)) {
                                System.out.println("You can only withdraw in multiples of 5 or 20");
                                amount = howMuch(scan);
                            }
                            customer.getChecking().withdraw(amount);
                            withdrawBills(scan,amount, true);
                            printReciept(choice,true,amount, customer.getChecking());
                        }
                        else {printReciept(choice,false,amount, customer.getChecking());}
                    }
                    else { //if savings
                        if (customer.getSavings().getBal() >= amount) {
                            while (!(amount % 5 == 0)) {
                                System.out.println("You can only withdraw in multiples of 5 or 20");
                                amount = howMuch(scan);
                            }
                            withdrawBills(scan, amount, true);
                            customer.getSavings().withdraw(amount);
                            printReciept(choice,true,amount, customer.getSavings());
                        }
                        else {printReciept(choice,false,amount, customer.getChecking());}
                    }
                    transactionID++;
                    break;
                case 2:
                    amount = howMuch(scan);
                    if (fromAccount(scan)) {
                        customer.getChecking().deposit(amount);
                        printReciept(choice,true,amount, customer.getChecking());
                    }
                    else {
                        customer.getSavings().deposit(amount);
                        printReciept(choice, true, amount, customer.getSavings());
                    }
                    transactionID++;
                    break;
                case 3:
                    amount = howMuch(scan);
                    if (fromAccount(scan)) {
                        printReciept(choice, customer.getChecking().transfer(customer.getSavings(), amount), amount, customer.getChecking());
                    }
                    else {
                        printReciept(choice, customer.getSavings().transfer(customer.getChecking(), amount), amount, customer.getSavings());
                    }
                    transactionID++;
                    break;
                case 4:
                    printReciept(choice, true, 0,null); // values are printed onto the receipt
                    break;
                case 5:
                    choosePIN(scan);
                    printReciept(choice, true, 0, null);
                    break;
                case 6:
                    break;
            }
        }
    }

    /**
     * Prompts the user to determine how much money they want to move
     *
     * @param scan Scanner object that allows for user input
     * @return the amount the of money that will be moved or changed
     */
    private double howMuch(Scanner scan) {
        System.out.print("How much money would you like to move?\n> ");
        double money = scan.nextDouble();
        scan.nextLine();
        return money;
    }

    /**
     * Asks the user to decide which account is going to be affected
     *
     * @param scan Scanner object that allows for user input
     * @return checking or savings in the form of true or false; true for checking and false for savings
     */
    private boolean fromAccount(Scanner scan) {
        System.out.println("\nWhich account are you going to change (the account that will lose money when transferring)");
        System.out.print("\n1. Checking Account\n2. Savings Account\n> ");
        int choice = scan.nextInt();
        scan.nextLine();
        while (choice != 1 && choice != 2) {
            System.out.println("Enter a valid option");
            System.out.print("\n1. Checking Account\n2. Savings Account\n> ");
            choice = scan.nextInt();
            scan.nextLine();
        }
        return choice == 1; // true for checking and false for savings
    }

    /**
     * Checks to see that the PIN is the same as the one that is stored
     *
     * @param scan Scanner object that allows for user input
     */
    private void checkPIN(Scanner scan) {
        System.out.print("\nPlease enter your PIN \n> ");
        String input = scan.nextLine();
        while (!customer.confirmPIN(input)) {
            System.out.print("Wrong PIN please try again\n> ");
            input = scan.nextLine();
        }
    }

    /**
     * Prompts the user to choose a valid PIN
     * <p>
     * Continues until they enter a valid PIN
     *
     * @param scan Scanner object to allow for user input
     */
    private void choosePIN(Scanner scan) {
        String newPIN = "";
        while (!(newPIN.length() == 4)) { // make a new thing maybe use string?
            System.out.print("\nEnter a 4 character PIN (letters and numbers both work):\n> ");
            newPIN = scan.nextLine();
        }
        customer.setPIN(newPIN);
    }

    private void withdrawBills(Scanner scan, double amount, boolean again) {
        if (!again) {System.out.println("Choose a valid amount of bills");}
        int maxFives = (int) amount / 5;
        int maxTwenties = (int) amount / 20;
        System.out.println("You can get up to " + maxFives + " $5 bills and " + maxTwenties + " $20 bills");
        System.out.print("How many $20 bills do you want?\n> ");
        int numTwenties = scan.nextInt();
        scan.nextLine();
        if (numTwenties > maxTwenties) { //too many then run the method again
            withdrawBills(scan, amount, false);
        }
        else {
            printMsg += "You received " + numTwenties + " $20 bills and ";
            amount -= numTwenties * 20;
            printMsg += (int) amount/5 + " $5 bills\n";
        }
    }
}
