package co.paystack.example.tests;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;

import co.paystack.android.model.Card;
import co.paystack.example.BuildConfig;

/**
 * Created by Segun Famisa {segunfamisa@gmail.com} on 9/16/15.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class /*, sdk = 21, manifest = "src/main/AndroidManifest.xml"*/)
public class CardTest {

    private static final int YEAR_FUTURE = 2016;
    private static final int YEAR_PAST = 2014;
    private static final int MONTH_PAST = 8;

    private static final int YEAR = 2017;
    private static final int MONTH = 3;
    private static final int DAY = 17;

    private static final String CVC_3 = "123";
    private static final String CVC_4 = "1234";

    private static final String MASTER_CARD_NUMBER = "5105105105105100";

    private static final String DISCOVER_CARD_NUMBER = "6500000000000002";

    private static final String VISA_CARD_NUMBER = "4111111111111111";
    private static final String VISA_CARD_NUMBER_2 = "4342-5611-1111-1118";

    private static final String AMEX_CARD_NUMBER = "341111111111111";


    Calendar cal;

    @Before
    public void setup(){
        cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2015);
        cal.set(Calendar.MONTH, Calendar.MARCH);
        cal.set(Calendar.DAY_OF_MONTH, 17);
    }

    @Test
    public void testExpiredCardMonth() throws Exception{
        Card card = new Card.Builder(MASTER_CARD_NUMBER, 2015, MONTH_PAST, "123").build();
        assertSame(false, card.validExpiryDate());
    }

    @Test
    public void testExpiredCardYear() throws Exception{
        Card card = new Card.Builder(MASTER_CARD_NUMBER, YEAR_PAST, Calendar.DECEMBER+1, "123").build();
        assertSame(false, card.validExpiryDate());
    }

    @Test
    public void canInitializeCardWithBuilder() throws Exception{
        Card card = new Card.Builder(MASTER_CARD_NUMBER, 2017, 03, "123").build();
        assertTrue(card.isValid());
    }

    @Test
    public void testTypeDetectionMasterCard() throws Exception{
        Card card = new Card(MASTER_CARD_NUMBER, YEAR, MONTH, CVC_3);
        assertSame(Card.CardType.MASTERCARD, card.getType());
    }

    @Test
    public void testTypeDetectionAmericanExpress() throws Exception{
        Card card = new Card(AMEX_CARD_NUMBER, YEAR, MONTH, CVC_4);
        assertSame(Card.CardType.AMERICAN_EXPRESS, card.getType());
    }

    @Test
    public void testTypeDetectionVisaCard() throws Exception{
        Card card = new Card(VISA_CARD_NUMBER, YEAR, MONTH, CVC_3);
        assertSame(Card.CardType.VISA, card.getType());
    }

    @Test
    public void testTypeDetectionDiscoverCard() throws Exception{
        Card card = new Card(DISCOVER_CARD_NUMBER, YEAR, MONTH, CVC_3);
        assertSame(Card.CardType.DISCOVER, card.getType());
    }
}
