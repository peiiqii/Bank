import bank.*;
import bank.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrivateBankTest {
    public PrivateBank privateBank;

    /* We need BeforeEach and AfterEach to delete and create new before and after every test
    * to ensure every test have the same condition before testing it
    * */
    @BeforeEach
    public void init() {
        try {
            privateBank = new PrivateBank("Bank1", 0.05, 0.1);

            privateBank.createAccount("User1");
            privateBank.addTransaction("User1", new Payment("03.05.2004", 1000, "payment1_user1"));

            privateBank.createAccount("User2");
            privateBank.addTransaction("User2", new Payment("16.01.1999", 1000, "payment1_user2"));
            privateBank.addTransaction("User2", new Payment("22.12.2002", 2000, "payment2_user2"));
            privateBank.addTransaction("User2", new Payment("09.05.2022", 3000, "payment3_user2"));

            privateBank.createAccount("User3");
            privateBank.addTransaction("User3", new Payment("20.11.2011", 1000, "payment1_user3"));
            privateBank.addTransaction("User3", new Payment("04.08.1997", -2000, "payment2_user3"));
            privateBank.addTransaction("User3", new Payment("09.05.2022", 3000, "payment3_user3"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    public void deletefiles() {
        File del = new File("./accounts/");
        for (File file : del.listFiles()) {
            file.delete();
        }
    }

    @Test
    void TestKonstruktor() {
        assertEquals("Bank1", privateBank.getName());
        assertEquals(0.05, privateBank.getIncomingInterest());
        assertEquals(0.1, privateBank.getOutgoingInterest());
    }

    @Test
    public void TestCopyKonstruktor() throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException, InterestExceptions {
        assertEquals(privateBank, new PrivateBank(privateBank));
        //was Falsches
    }

    @Test
    public void TestEquals() throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException, InterestExceptions {
        PrivateBank pb = new PrivateBank("Bank1", 0.05, 0.1);

        try {
            pb.createAccount("User1");
            pb.addTransaction("User1", new Payment("03.05.2004", 1000, "payment1_user1"));

            pb.createAccount("User2");
            pb.addTransaction("User2", new Payment("16.01.1999", 1000, "payment1_user2"));
            pb.addTransaction("User2", new Payment("22.12.2002", 2000, "payment2_user2"));
            pb.addTransaction("User2", new Payment("09.05.2022", 3000, "payment3_user2"));

            pb.createAccount("User3");
            pb.addTransaction("User3", new Payment("20.11.2011", 1000, "payment1_user3"));
            pb.addTransaction("User3", new Payment("04.08.1997", -2000, "payment2_user3"));
            pb.addTransaction("User3", new Payment("09.05.2022", 3000, "payment3_user3"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }//AssertNotThrows
        assertEquals(pb, privateBank);
    }

    @ParameterizedTest
    @ValueSource(strings = {"NewUser1", "NewUser2", "NewUser3"})
    public void TestCreateAccount(String account) throws AccountAlreadyExistsException, IOException, TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, InterestExceptions {
        privateBank.createAccount(account);
        Exception exception = assertThrows(AccountAlreadyExistsException.class, () -> {
            privateBank.createAccount(account);
        });
    }

    @Test
    public void AddTransactionTest() {
        Exception exception = assertThrows(AccountDoesNotExistException.class, () -> {
            privateBank.addTransaction("NewAccount", new Payment("11.11.2011", 100, "new_payment", 1, 1));
        });

        Exception exception1 = assertThrows(TransactionAlreadyExistException.class, () -> {
            privateBank.addTransaction("User1", new Payment("03.05.2004", 1000, "payment1_user1", 0.05, 0.1));
        });
    }

    @Test
    public void ContainsTransactionTest() throws InterestExceptions, TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException {
        Payment p1 = new Payment("03.05.2004", 1000, "payment1_user1"); //existed
        Payment p2 = new Payment("23.12.2012", 2000, "payment2_user1"); //not existed
        try {
            privateBank.addTransaction("User1", p1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(privateBank.containsTransaction("User1", p1));
        assertFalse(privateBank.containsTransaction("User1", p2));
    }

    @Test
    public void RemoveTransactionTest() throws InterestExceptions, TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException {
        Payment p1 = new Payment("03.05.2004", 1000, "payment1_user1", 0.05, 0.1);
        Exception exception = assertThrows(AccountDoesNotExistException.class, () -> {
            privateBank.removeTransaction("NewAccount", p1);
        });
        Exception exception1 = assertThrows(TransactionDoesNotExistException.class, () -> {
            privateBank.removeTransaction("User2", p1);
        });
        try {
            privateBank.addTransaction("User1", p1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertTrue(privateBank.containsTransaction("User1", p1));

        try {
            privateBank.removeTransaction("User1", p1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        assertFalse(privateBank.containsTransaction("User1", p1));
    }

    @Test
    public void GetAccountBalanceTest() throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException, InterestExceptions {
        assertEquals(950, privateBank.getAccountBalance("User1"));
        assertEquals(5700.0, privateBank.getAccountBalance("User2"));
        assertEquals(1600.0, privateBank.getAccountBalance("User3"));
    }

    @Test
    public void GetTransactionsSortedTest() throws TransactionAlreadyExistException, AccountAlreadyExistsException, AccountDoesNotExistException, TransactionAttributeException, IOException, InterestExceptions {
        assertTrue(privateBank.getTransactionsSorted("User2", true).get(0).getAmount() == 1000);
        assertTrue(privateBank.getTransactionsSorted("User2", true).get(1).getAmount() == 2000);
        assertTrue(privateBank.getTransactionsSorted("User2", true).get(2).getAmount() == 3000);

        assertTrue(privateBank.getTransactionsSorted("User2", false).get(0).getAmount() == 3000);
        assertTrue(privateBank.getTransactionsSorted("User2", false).get(1).getAmount() == 2000);
        assertTrue(privateBank.getTransactionsSorted("User2", false).get(2).getAmount() == 1000);
    }

    @Test
    public void getTransactionsByTypeTest() throws InterestExceptions, TransactionAlreadyExistException, AccountAlreadyExistsException, TransactionAttributeException, IOException, AccountDoesNotExistException {
        List<Transaction> list = new ArrayList<Transaction>();
        list.add(new Payment("yesterday", 1000, "payment1", 1, 1));
        list.add(new Payment("today", 2000, "payment2", 1, 1));
        list.add(new Payment("tomorrow", -3000, "payment3", 1, 1));
        privateBank.createAccount("User4", list);

        assertTrue(privateBank.getTransactionsByType("User4", true).get(0).getAmount() == 1000);
        assertTrue(privateBank.getTransactionsByType("User4", true).get(1).getAmount() == 2000);
        assertTrue(privateBank.getTransactionsByType("User4", false).get(0).getAmount() == -3000);
    }
}
