package bank.exceptions;
import bank.Transaction;

import java.util.List;

public class TransactionAlreadyExistException extends Exception{
    public TransactionAlreadyExistException(){
        super("Transaction exist!");
    }
}
