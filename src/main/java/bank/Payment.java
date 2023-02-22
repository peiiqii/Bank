package bank;

import bank.exceptions.InterestExceptions;

public class Payment extends Transaction{

    private double incomingInterest;
    private double outgoingInterest;

    /**
     * Returns the incoming interest of payment
     * @return incoming interest
     */
    public double getIncomingInterest() {
        return incomingInterest;
    }

    /**
     * Sets new incoming interest
     * Checks the value of the incoming interest, value can only between 0 and 1
     * @param incomingInterest new incoming interest
     */
    public void setIncomingInterest(double incomingInterest) throws InterestExceptions {
        if(incomingInterest >= 0 && incomingInterest <= 1){
            this.incomingInterest = incomingInterest;
        }else{
            throw new InterestExceptions();
        }
    }

    /**
     * Returns the outgoing interest of payment
     * @return outgoing interest
     */
    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    /**
     * Sets new outgoing interest
     * Checks the value of the outgoing interest, value can only between 0 and 1
     * @param outgoingInterest new outgoing interest
     */
    public void setOutgoingInterest(double outgoingInterest) throws InterestExceptions {
        if(outgoingInterest >= 0 && outgoingInterest <= 1){
            this.outgoingInterest = outgoingInterest;
        }else{
            throw new InterestExceptions();
        }
    }

    /**
     *
     *  Creates a new payment object
     *  @param Date             Date in format DD.MM.YYYY
     *  @param Amount           Amount of Transaction
     *  @param Description      Description of the Transaction
     */
    public Payment(String Date, double Amount, String Description){
        super(Date, Amount, Description);
    }

    /**
     *
     *  Creates a new payment object
     *  @param Date             Date in format DD.MM.YYYY
     *  @param Amount           Amount of Transaction
     *  @param Description      Description of the Transaction
     *  @param IncomingInterest Incoming interest of the transaction(%), value between 0 and 1
     *  @param OutgoingInterest Outgoing interest of the transaction(%), value between 0 and 1
     */
    public Payment(String Date, double Amount, String Description, double IncomingInterest, double OutgoingInterest) throws InterestExceptions {
        super(Date, Amount, Description);
        setIncomingInterest(IncomingInterest);
        setOutgoingInterest(OutgoingInterest);
    }

    /**
     * Creates a copy of a payment object
     *
     * @param p creates a copy of payment
     */
    public Payment(Payment p) throws InterestExceptions {
        this(p.date, p.amount,p.description,p.incomingInterest, p.outgoingInterest);
    }

    /**
     * Combines all class attributes into a string and outputs the string
     *
     * @return string of all attributes
     */
    @Override
    public String toString(){
        return super.toString() + ", IncomingInterest: " + this.incomingInterest + ", OutgoingInterest: " + this.outgoingInterest;
    }

    /**
     *
     * Calculates new value of amount
     *
     * @return new amount
     */
    @Override
    public double calculate() {
        double ergebnis = 0;
        if(getAmount() > 0){
            ergebnis = getAmount() - (getAmount() * getIncomingInterest());
        }
        else if(getAmount() < 0){
            ergebnis = getAmount() + (getAmount() * getOutgoingInterest());
        }
        return ergebnis;
    }

    /**
     * Checks if the Objects are equal
     *
     * @param obj
     * @return true if equal otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Payment payment) {
            if ((super.equals(payment)) && (this.incomingInterest == payment.incomingInterest) && (this.outgoingInterest == payment.outgoingInterest)) {
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }
}
