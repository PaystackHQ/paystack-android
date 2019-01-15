package co.paystack.example.tests;

import android.content.Context;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.invocation.MockitoMethod;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.regex.Pattern;

import co.paystack.android.api.print.PrinterTemplate;
import co.paystack.android.model.Card;
import co.paystack.android.model.Purchase;
import co.paystack.android.model.ReceiptHeader;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class PrinterTest {

    @Mock
    PrinterTemplate printerTemplate;

    private ReceiptHeader header;


    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);

        //Set up printer header
        header =new ReceiptHeader("DevMike Co.",
                "30 Ikeja street, Lagos state",
                "devmike@jadebyte.com", "08123456778");
        printerTemplate.print(header, null);
    }

    @Test
    public void testCardName(){
        Purchase purchase =new Purchase();
        purchase.setCardName(Card.Printer.CARD_NAME);
        assertSame(Card.Printer.CARD_NAME, purchase.getCardName());
    }

    @Test
    public void testAmount(){
        Purchase purchase = new Purchase();
        purchase.setAmount(Card.Printer.AMOUNT);
        assertEquals(Card.Printer.AMOUNT, purchase.getAmount());
    }

    @Test
    public void testCardNumber(){
        Purchase purchase = new Purchase();
        purchase.setCardNumber(Card.Printer.CARD_NUMBER);
        assertThat(purchase.getCardNumber(), containsString("*"));
    }

    @Test
    public void testItem(){
        Purchase purchase = new Purchase();
        purchase.setItem(Card.Printer.ITEM);
        assertSame(Card.Printer.ITEM, purchase.getItem());
    }

    @Test
    public void testTransactionCharge(){
        Purchase purchase = new Purchase();
        purchase.setTransactionCharge(Card.Printer.TRANSACTION_CHARGE);
        assertEquals(Card.Printer.TRANSACTION_CHARGE, purchase.getTransactionCharge());
    }

    @Test
    public void testTransactionMessage(){
        Purchase purchase = new Purchase();
        purchase.setTransactionMessage(Card.Printer.TRANSACTION_MESSAGE);
        assertSame(Card.Printer.TRANSACTION_MESSAGE, purchase.getTransactionMessage());
    }

    @Test
    public void testTransactionStatus(){
        Purchase purchase = new Purchase();
        purchase.setTransactionStatus(Card.Printer.TRANSACTION_STATUS);
        assertSame(Card.Printer.TRANSACTION_STATUS, purchase.getTransactionStatus());
    }

    @Test
    public void testBusinessName(){

        assertNotNull(header.getBusinessName());
    }

    @Test
    public void testAddress(){
        assertNotNull(header.getAddress());
    }

    @Test
    public void testPrinterEmail(){
        assertTrue(isEmailValid(header.getEmail()));
    }

    @Test
    public void testPhone(){
        assertNotNull(header.getPhone());
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        java.util.regex.Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
