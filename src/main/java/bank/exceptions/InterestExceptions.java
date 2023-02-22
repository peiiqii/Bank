package bank.exceptions;
import bank.Transaction;

import java.util.List;

public class InterestExceptions extends Exception{
    public InterestExceptions(){
        super("Value can only between 0 and 1.");
    }
}