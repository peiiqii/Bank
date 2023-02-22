package bank.exceptions;

import bank.Transaction;

import java.util.List;

public class TransactionAttributeException extends Exception{
    public TransactionAttributeException(){
        super("Validation failed!");
    }
}
