package bank.exceptions;
import bank.Transaction;

public class TransactionDoesNotExistException extends Exception{
    public TransactionDoesNotExistException(){
        super("Transaction does not exist!");
    }
}
