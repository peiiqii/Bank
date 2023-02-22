import bank.Payment;
import bank.Transfer;
import bank.exceptions.InterestExceptions;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentTest {
    Payment payment1;
    Payment payment2;

    @BeforeEach
    public void init() throws InterestExceptions {
        payment1 = new Payment("05.12.2022", 1000, "Payment_Test", 0.05, 0.1);
        payment2 = new Payment("05.12.2022", -1000, "Payment_Test", 0.05, 0.1);
    }

    @Test
    public void TestKonstruktor(){
        assertEquals("05.12.2022", payment1.getDate());
        assertEquals(1000, payment1.getAmount());
        assertEquals("Payment_Test", payment1.getDescription());
        assertEquals(0.05, payment1.getIncomingInterest());
        assertEquals(0.1, payment1.getOutgoingInterest());
    }

    @Test
    public void TestCopyKonstruktor() throws InterestExceptions {
        assertEquals(payment1, new Payment(payment1));
    }

    @Test
    public void TestCalculate(){
        assertEquals(950,payment1.calculate());
        assertEquals(-1100,payment2.calculate());
    }

    @Test
    public void TestEqual() {
        assertFalse(payment1.equals(payment2));
        payment1.setAmount(payment2.getAmount());
        assertEquals(payment1,payment2);
    }

    @Test
    public void TestToString(){
        assertEquals("Date: 05.12.2022, Amount: 950.0, Description: Payment_Test, IncomingInterest: 0.05, OutgoingInterest: 0.1", payment1.toString());
    }
}
