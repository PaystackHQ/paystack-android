package co.paystack.example.tests;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.invocation.MockitoMethod;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import co.paystack.android.api.print.PrinterTemplate;
import co.paystack.android.model.Card;
import co.paystack.android.model.Purchase;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;


@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class PrinterTest {

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);

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
        purchase.setTransactionCharge(12);
        assertEquals(12, purchase.getTransactionCharge());
    }

}
