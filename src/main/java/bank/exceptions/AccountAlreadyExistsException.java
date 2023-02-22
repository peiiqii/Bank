package bank.exceptions;
//import bank.Transaction;

public class AccountAlreadyExistsException extends Exception{
    public AccountAlreadyExistsException(){
        super("Account exist!");
    }
}
