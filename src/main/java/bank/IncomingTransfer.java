package bank;

/**
 * IncomingTransfer class take care of all incoming Transfer
 * inherits from Transfer
 */
public class IncomingTransfer extends Transfer{
    /**
     * Constructor for IncomingTransfer
     * @param trans Transfer Object
     */
    public IncomingTransfer(Transfer trans){
        super(trans);
    }

    /**
     * Constructor for IncomingTransfer
     * @param date
     * @param amount
     * @param description
     * @param sender
     * @param recipient
     */
    public IncomingTransfer(String date, double amount, String description, String sender, String recipient){
        super(date, amount, description, sender, recipient);
    }

    @Override
    public String toString(){
        return "IncomingTransfer: " + super.toString();
    }
}

