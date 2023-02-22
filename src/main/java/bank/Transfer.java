package bank;

public class Transfer extends Transaction{

    private String sender;
    private String recipient;

    /**
     * Returns the Sender of Transaction
     * @return sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets new Sender of the transaction
     *
     * @param sender new Sender of the transaction
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Returns the Recipient of Transaction
     * @return recipient
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Sets new Recipient of the transaction
     *
     * @param recipient new Recipient of the transaction
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     *
     *  Creates a new transfer object
     *  @param Date             Date in format DD.MM.YYYY
     *  @param Amount           Amount of Transaction
     *  @param Description      Description of the Transaction
     */
    public Transfer(String Date, double Amount, String Description){
        super(Date,Amount,Description);
    }

    /**
     *  Creates a new transfer object
     *
     *  @param Date             Date in format DD.MM.YYYY
     *  @param Amount           Amount of Transaction, can only a positive value
     *  @param Description      Description of the Transaction
     *  @param Sender           Sender of the Transaction
     *  @param Recipient        Recipient of the Transaction
     */
    public Transfer(String Date, double Amount, String Description, String Sender, String Recipient) {
        super(Date, Amount, Description);
        this.sender=Sender;
        this.recipient=Recipient;
    }

    /**
     * Creates a copy of a transfer object
     *
     * @param t creates a copy of transfer
    */
    public Transfer(Transfer t){
        this(t.date, t.amount,t.description,t.sender, t.recipient);
    }

    /**
     * Combines all class attributes into a string and outputs the string
     *
     * @return string of all attributes
     */
    @Override
    public String toString(){
        return super.toString() + ", Sender: " + getSender() + ", Recipient: " + getRecipient();
    }

    /**
     *
     * Calculates new value of amount
     *
     * @return value of the amount if the given amount is positive
     */
    @Override
    public double calculate(){
        if (getAmount() >= 0) {
            return getAmount();
        } else {
            System.out.println("Fehlerhafte Eingabe.");
        }
        return getAmount();
    }
    /**
     * Checks if the Objects are equal
     *
     * @param obj
     * @return true if equal otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Transfer transfer) {
            if ((super.equals(transfer)) && (this.sender == transfer.sender) && (this.recipient == transfer.recipient)) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }
}