public class Account {

    boolean type;
    private double balance;
    public Account(boolean type) {
        this.type = type; //true for checking; false for savings
        balance = 0.0;
    }

    public double getBal() {return balance;}
    public boolean getType() {return type;}

    public void deposit(double amount) {balance += amount;}

    public void withdraw(double amount) {balance -= amount;}

    public boolean transfer(Account receiver, double amount) {
        if (balance >= amount) {
            receiver.deposit(amount);
            withdraw(amount);
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        if (type) {
            return "checking";
        }
        return "savings";
    }



}
