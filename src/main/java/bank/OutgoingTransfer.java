package bank;

/**
 * OutgoingTransfer class handles all outgoing Transfer
 * inherits from Transfer
 */
public class OutgoingTransfer extends Transfer {

    /**
     * Constructor for OutgoingTransfer
     * @param trans Transfer Object
     */
    public OutgoingTransfer(Transfer trans) {
        super(trans);
    }

    /**
     * Constructor for OutgoingTransfer
     * @param date
     * @param amount
     * @param description
     * @param sender
     * @param recipient
     */
    public OutgoingTransfer(String date, double amount, String description, String sender, String recipient){
        super(date, amount, description, sender, recipient);
    }

    /**
     * overwritten from Transfer
     * @return negative Amount
     */
    @Override
    public double calculate(){
        return (-1 * super.calculate());
    }

    @Override
    public String toString(){
        return "OutgoingTransfer: " + super.toString();
    }

}
