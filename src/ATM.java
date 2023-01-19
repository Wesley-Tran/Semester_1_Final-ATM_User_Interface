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
            System.out.println("To get started with this ATM, you need to start an account");
            System.out.print("Enter your name:\n> ");
            name = scan.nextLine();
            customer = new Customer(name, 0); //give the customer a temp PIN and immediately tell them to set it
            choosePIN(scan);
        }
        while (cont) {
            checkPIN(scan);
            printGUI();
            displayMsg();
            int choice = scan.nextInt();
            scan.nextLine();
            if (choice==6) {break;}
            processChoice(choice, scan);
            //put reciept for what the user did
            System.out.print("Would you like to do anything else? (y/n)\n> ");
            String temp = scan.nextLine();
            if (temp.equals("n")) { //program will end
                cont = false;
                displayMsg();
                System.out.println("Come again soon!");
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
        printMsg += "********************\n";
        printMsg += "Welcome to the ATM " + customer.getName() + "\n";
        printMsg += "********************\n\n";
        printMsg += "Please choose an option: \n";
        printMsg += "1. Withdraw money\n";
        printMsg += "2. Deposit money\n";
        printMsg += "3. Transfer money between accounts\n";
        printMsg += "4. Get account balances\n";
        printMsg += "5. Change PIN\n";
        printMsg += "6. Exit\n> ";
    }

    private void printReciept(int choice, boolean transaction, double amount, Account account) {
        String msg = "";
        String condition = "";
        if (!transaction) {condition = "failed to";}

        switch(choice) {
            case 1:
                if (!transaction) {amount = 0;}
                msg += "withdrew $" + amount + " from " + account + "\n* Transaction ID: " + transactionID;
                break;
            case 2:
                msg += condition + "deposited $" + amount + " into " + account + "         *\n* Transaction ID: " + transactionID;
                break;
            case 3:
                String acc2 = "checking";
                if (account.getType()) {acc2 = "savings";}
                msg += "transfer of $" + amount + " from " + account + " to " + acc2 + " account: " + condition + "\n* Transaction ID: " + transactionID ;
                break;
            case 4:
                printMsg += "* Your savings account has: $" + customer.getSavings().getBal() + "*\n";
                printMsg += "* Your checking account has: $" + customer.getChecking().getBal() + "*\n";
                break;
            case 5:
                msg += "changed PIN: complete";
                break;
            case 6:
                break;
        }
        printMsg += "**********************************************\n";
        printMsg += "*                                           *\n";
        printMsg += "*                                          *\n";
        printMsg += "* " + msg + "                            *\n";
        printMsg += "*                                          *\n";
        printMsg += "*                                           *\n";
        printMsg += "**********************************************\n";

    }

    /**
     * Runs a method or does an action based on the choice that the user chose
     *
     * @param choice the option that the user chose and the action that they wanted to make
     * @param scan Scanner object that allows for user input
     */
    private void processChoice(int choice, Scanner scan) {
        if (!(choice > 0 && choice < 7)) {
            System.out.println("Please enter a valid option");
        } else {
            switch(choice) {
                case 1:
                    double amount = howMuch(scan);
                    if (fromAccount(scan)) { // decides which one to withdraw from and how much
                        if (customer.getChecking().getBal() >= amount) {
                            customer.getChecking().withdraw(amount);
                            printReciept(choice,true,amount, customer.getChecking());
                        }
                        else {printReciept(choice,false,amount, customer.getChecking());}
                    }
                    else {
                        if (customer.getSavings().getBal() >= amount) {
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
                        printReciept(choice, customer.getSavings().transfer(customer.getChecking(), howMuch(scan)), amount, customer.getSavings());
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
        System.out.println("Which account are you going to change (the account that will lose money when transferring)");
        System.out.print("Enter 1 for checking and 2 for savings account\n > ");
        int choice = scan.nextInt();
        scan.nextLine();
        return choice == 1; // true for checking and false for savings
    }

    /**
     * Checks to see that the PIN is the same as the one that is stored
     *
     * @param scan Scanner object that allows for user input
     */
    private void checkPIN(Scanner scan) {
        System.out.print("Please enter your PIN \n> ");
        int input = scan.nextInt();
        scan.nextLine();
        while (!customer.confirmPIN(input)) {
            System.out.print("Wrong PIN please try again\n> ");
            input = scan.nextInt();
            scan.nextLine();
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
        int newPIN = 0;
        while (!(newPIN > 999 && newPIN < 10000)) { // make a new thing maybe use string?
            System.out.print("Enter a 4 digit PIN (stick to numbers please):\n> ");
            newPIN = scan.nextInt();
            scan.nextLine();
        }
        customer.setPIN(newPIN);
    }

}
