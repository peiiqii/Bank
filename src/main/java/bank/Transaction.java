package bank;

public abstract class Transaction implements CalculateBill{
    //protected - members can only be accessed in its own package
    protected String date;
    protected double amount;
    protected String description;

    /**
     * Returns the Date of Transaction in Format DD.MM.YYYY
     *
     * @return date
     */
    public String getDate() { return date; }

    /**
     * Sets new Date of the transaction
     *
     * @param date new Date of the transaction
     */
    public void setDate(String date) { this.date = date;  }

    /**
     * Returns the Amount of Transaction
     * @return amount
     */
    public double getAmount() { return amount; }

    /**
     * Sets new Amount of the transaction
     *
     * @param amount new amount of the transaction
     */
    public void setAmount(double amount) {this.amount = amount; }

    /**
     * Returns the Description of Transaction
     * @return description
     */
    public String getDescription() { return description; }

    /**
     * Sets new Description of the transaction
     *
     * @param description new description of the transaction
     */
    public void setDescription(String description) { this.description = description; }


    /**
     *
     *  Creates a new Transaction Object
     *
     *  @param Date             Date in format DD.MM.YYYY
     *  @param Amount           Amount of Transaction
     *  @param Description      Description of the Transaction
     */
    public Transaction(String Date, double Amount, String Description) {
        setDate(Date);
        setAmount(Amount);
        setDescription(Description);
    }

    /**
     * Combines all class attributes into a string and outputs the string
     *
     * @return string of all attributes
     */
    @Override
    public String toString() {
        return "Date: " + getDate() + ", Amount: " + calculate() + ", Description: " + getDescription();
    }

    /**
     * Checks if the two Objects are equal
     *
     * @param obj
     * @return If both objects are equal, then equals() returns true
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Transaction transaction) {
            if ((this.date.equals(transaction.date)) && (this.amount == transaction.amount) && (this.description.equals(transaction.description))) {
                return true;
            } else {
                return false;
            }
        }else {
            return false;
        }
    }
}
