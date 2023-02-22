package bank;

import bank.exceptions.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.*;

public class PrivateBank implements Bank{
    private static GsonBuilder builder = new GsonBuilder()
            .registerTypeAdapter(Payment.class, new CustomSerializer())
            .registerTypeAdapter(IncomingTransfer.class, new CustomSerializer())
            .registerTypeAdapter(OutgoingTransfer.class, new CustomSerializer())
            .registerTypeAdapter(Transaction.class, new CustomSerializer())
            .setPrettyPrinting();
    protected String name;
    protected double incomingInterest;
    protected double outgoingInterest;
    protected Map<String, List<Transaction>> accountsToTransaction = new HashMap<String, List<Transaction>>();
    private String directoryName;

    /**
     * Returns the name of the private Bank
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets new Name of the private Bank
     *
     * @param name new Name of the private Bank
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the incoming interest of payment
     *
     * @return incoming interest
     */
    public double getIncomingInterest() {
        return incomingInterest;
    }

    /**
     * Sets new incoming interest
     * Checks the value of the incoming interest, value can only between 0 and 1
     *
     * @param incomingInterest new incoming interest
     */
    public void setIncomingInterest(double incomingInterest) {
        if (incomingInterest >= 0 && incomingInterest <= 1) {
            this.incomingInterest = incomingInterest;
        } else {
            System.out.println("Eine fehlerhafte Eingabe.");
        }
    }

    /**
     * Sets new directory name
     *
     * @param directoryName Name of directory
     */
    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    /**
     * Returns the directory name
     *
     * @return directoryName
     */
    public String getDirectoryName() {
        return directoryName;
    }


    /**
     * Returns the outgoing interest of payment
     *
     * @return outgoing interest
     */
    public double getOutgoingInterest() {
        return outgoingInterest;
    }

    /**
     * Sets new outgoing interest
     * Checks the value of the outgoing interest, value can only between 0 and 1
     *
     * @param outgoingInterest new outgoing interest
     */
    public void setOutgoingInterest(double outgoingInterest) {
        if (outgoingInterest >= 0 && outgoingInterest <= 1) {
            this.outgoingInterest = outgoingInterest;
        } else {
            System.out.println("Eine fehlerhafte Eingabe.");
        }
    }

    /**
     * Creates a new PrivateBank object
     *
     * @param Name             Name of the new private Bank
     * @param IncomingInterest Incoming interest of the transaction(%), value between 0 and 1
     * @param OutgoingInterest Outgoing interest of the transaction(%), value between 0 and 1
     */
    public PrivateBank(String Name, double IncomingInterest, double OutgoingInterest) throws IOException, TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, InterestExceptions {
        setName(Name);
        setIncomingInterest(IncomingInterest);
        setOutgoingInterest(OutgoingInterest);
        readAccounts();
    }

    /**
     * Creates a copy of a PrivateBank object
     *
     * @param pb creates a copy of payment
     */
    public PrivateBank(bank.PrivateBank pb) throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException, InterestExceptions {
        this(pb.name, pb.incomingInterest, pb.outgoingInterest);
        this.accountsToTransaction = pb.accountsToTransaction;
    }


    /**
     * Combines all class attributes into a string and outputs the string
     *
     * @return string of all attributes
     */
    @Override
    public String toString() {
        return "PrivateBank{" +
                "name='" + name + '\'' +
                ", incomingInterest=" + incomingInterest +
                ", outgoingInterest=" + outgoingInterest +
                ", accountsToTransaction=" + accountsToTransaction +
                ", directoryName='" + directoryName + '\'' +
                '}';
    }

    /**
     * Checks if the Objects are equal
     *
     * @param obj
     * @return true if equal otherwise false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof bank.PrivateBank privatebank) {
            if ((this.name.equals(privatebank.name)) && (this.incomingInterest == privatebank.incomingInterest) && (this.outgoingInterest == privatebank.outgoingInterest)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Adds an account to the bank.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     */
    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException, TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException, InterestExceptions {
        if (accountsToTransaction.containsKey(account)) {
            throw new AccountAlreadyExistsException();
        } else {
            accountsToTransaction.put(account, new ArrayList<Transaction>());
            writeAccount(account);
            System.out.println("Account sucessfully created!");
        }
    }

    /**
     * Adds an account (with specified transactions) to the bank.
     * Important: duplicate transactions must not be added to the account!
     *
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException    if the account already exists
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws    if the validation check for certain attributes fail
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions) throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, InterestExceptions {
        if (accountsToTransaction.containsKey(account)) {
            throw new AccountAlreadyExistsException();
        } else if (accountsToTransaction.containsKey(account) && accountsToTransaction.containsValue(transactions)) {
            throw new TransactionAlreadyExistException();
        } else if (account.isEmpty()) {
            throw new TransactionAttributeException();
        } else {
            if (transactions instanceof Payment payment) {
                payment.setIncomingInterest(bank.PrivateBank.this.incomingInterest);
                payment.setOutgoingInterest(bank.PrivateBank.this.outgoingInterest);
            }
            accountsToTransaction.put(account, transactions);
            try{writeAccount(account);}
            catch (IOException e){ e.printStackTrace();}
            System.out.println("Account sucessfully created!");
        }
    }

    /**
     * Adds a transaction to an already existing account.
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException if the transaction already exists
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionAttributeException    if the validation check for certain attributes fail
     */
    @Override
    public void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, InterestExceptions, IOException {
        if(!accountsToTransaction.containsKey(account)){
            throw new AccountDoesNotExistException();
        }
        if(accountsToTransaction.get(account).contains(transaction)){
            throw new TransactionAlreadyExistException();
        }
        if(transaction instanceof Payment){
            ((Payment) transaction).setIncomingInterest(this.incomingInterest);
            ((Payment) transaction).setOutgoingInterest(this.outgoingInterest);
        }
        accountsToTransaction.get(account).add(transaction);

        this.writeAccount(account);
        }

    /**
     * Removes a transaction from an account. If the transaction does not exist, an exception is
     * thrown.
     *
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException     if the specified account does not exist
     * @throws TransactionDoesNotExistException if the transaction cannot be found
     */
    @Override
    public void removeTransaction(String account, Transaction transaction) throws AccountDoesNotExistException, TransactionDoesNotExistException {
        if (!(accountsToTransaction.containsKey(account)))
        {
            throw new AccountDoesNotExistException();
        }
        else if (!(accountsToTransaction.get(account).contains(transaction)))
        {
            throw new TransactionDoesNotExistException();
        }
        else
        {
            List<Transaction> transactionsList = accountsToTransaction.get(account);
            transactionsList.remove(transaction);
            accountsToTransaction.replace(account, transactionsList);
            try{writeAccount(account);}
            catch (IOException e){ e.printStackTrace();}
            System.out.println("Transaction successfully removed");
        }
    }

    /**
     * Checks whether the specified transaction for a given account exists.
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException, InterestExceptions {
        List<Transaction> tempListe = accountsToTransaction.get(account);
        for(Transaction t : tempListe){
            if(t.equals(transaction)){
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates and returns the current account balance.
     *
     * @param account the selected account
     * @return the current account balance
     */
    @Override
    public double getAccountBalance(String account) throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException, InterestExceptions {
        double balance = 0.0;
        for(Transaction transaction : accountsToTransaction.get(account)){
            balance += transaction.calculate();
        }
        return balance;
    }

    /**
     * Returns a list of transactions for an account.
     *
     * @param account the selected account
     * @return the list of all transactions for the specified account
     */
    @Override
    public List<Transaction> getTransactions(String account) {
        return accountsToTransaction.get(account);
    }

    /**
     * Returns a sorted list (-> calculated amounts) of transactions for a specific account. Sorts the list either in ascending or descending order
     * (or empty).
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted in ascending or descending order
     * @return the sorted list of all transactions for the specified account
     */
    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException, InterestExceptions {
        List<Transaction> SortedList = accountsToTransaction.get(account);
        if(asc){
            SortedList.sort(Comparator.comparingDouble(Transaction::calculate));
        }else{
            SortedList.sort(Comparator.comparingDouble(Transaction::calculate).reversed());
        }

        return SortedList;

    }

    /**
     * Returns a list of either positive or negative transactions (-> calculated amounts).
     *
     * @param account  the selected account
     * @param positive selects if positive or negative transactions are listed
     * @return the list of sorted transactions
     */
    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException, InterestExceptions {
        List<Transaction> SortedByTypeList = new ArrayList<>();
        for (Transaction transaction : accountsToTransaction.get(account)) {
            if (positive && transaction.calculate() >= 0)
                SortedByTypeList.add(transaction);
            else if (!positive && transaction.calculate() < 0)
                SortedByTypeList.add(transaction);
        }
        return SortedByTypeList;
    }

    /**
     * Reads accounts from the file system
     * @throws IOException if the file does not exist
     * @throws AccountAlreadyExistsException if account already existed
     *
     */
    private void readAccounts() throws IOException, TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException, InterestExceptions {
        var customGson = builder.create();
        File dir = new File("./accounts/");

        for(File file : dir.listFiles()){
            FileReader reader = new FileReader(file);
            JsonReader jreader = new JsonReader(reader);

            Transaction[] transarray = customGson.fromJson(jreader, Transaction[].class);
            this.createAccount(file.getName().replace(".json",""), new ArrayList<Transaction>(Arrays.asList(transarray)));

            reader.close();
        }
    }
    /**
     * Persists the accounts in the file system (serialize and save)
     *
     * @param account the account to be persisted
     * @throws IOException if file does not exist
     * @return
     */
    private void writeAccount(String account) throws IOException {
        CustomSerializer ser = new CustomSerializer();
        List<JsonElement> jsonArr = new LinkedList<JsonElement>();
        for(Transaction t : accountsToTransaction.get(account)) {
            jsonArr.add(ser.serialize(t, null, null));
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Writer writer = new FileWriter("./accounts/" + account + ".json");
        gson.toJson(jsonArr, writer);
        writer.close();
    }


    @Override
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException{
        accountsToTransaction.remove(account);
        var file = new File("./accounts/" + account + ".json");
        if (file.exists()){
            file.delete();
        } else {
            throw new AccountDoesNotExistException();
        }
    }

    @Override
    public List<String> getAllAccounts(){
        return new ArrayList<>(accountsToTransaction.keySet());
        /*List<String> accountList = new ArrayList<String>();
        File folder = new File("./accounts/");
        if(folder.listFiles() != null)
            for (File file : folder.listFiles())
                accountList.add(file.getName());
        return accountList;*/
    }
}



