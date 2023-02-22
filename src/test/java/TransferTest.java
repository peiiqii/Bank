import bank.IncomingTransfer;
import bank.OutgoingTransfer;
import bank.Transfer;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TransferTest {

    Transfer transfer1;

    @BeforeEach
    public void init(){
        transfer1 = new Transfer("05.12.2022", 1000, "Transfer_Test", "Sender", "Recipient");
    }

    @Test
    public void TestKonstruktor(){
        assertEquals("05.12.2022", transfer1.getDate());
        assertEquals(1000, transfer1.getAmount());
        assertEquals("Transfer_Test", transfer1.getDescription());
        assertEquals("Sender", transfer1.getSender());
        assertEquals("Recipient", transfer1.getRecipient());
    }

    @Test
    public void TestCopyKonstruktor(){
        assertEquals(transfer1, new Transfer(transfer1));
    }

    @Test
    public void TestCalculate(){
        IncomingTransfer incoming = new IncomingTransfer(transfer1);
        OutgoingTransfer outgoing = new OutgoingTransfer(transfer1);

        assertEquals(1000, incoming.calculate());
        assertEquals(-1000, outgoing.calculate());
    }

    @Test
    public void TestEquals(){
        Transfer transfer2 = new Transfer("05.12.2022", 1000, "Transfer_Test", "Sender", "Recipient");
        assertTrue(transfer1.equals(transfer2));

        transfer1.setDescription("Transfer2");
        assertFalse(transfer1.equals(transfer2));
    }

    @Test
    public void TestToString(){
        assertEquals("Date: 05.12.2022, Amount: 1000.0, Description: Transfer_Test, Sender: Sender, Recipient: Recipient", transfer1.toString());
    }
}