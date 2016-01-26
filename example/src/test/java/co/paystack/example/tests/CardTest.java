package co.paystack.example.tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Calendar;

import co.paystack.android.model.Card;
import co.paystack.example.BuildConfig;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test the card class
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class /*, sdk = 21, manifest = "src/main/AndroidManifest.xml"*/)
public class CardTest {

  private static final int YEAR = Calendar.getInstance().get(Calendar.YEAR);
  private static final int MONTH = Calendar.getInstance().get(Calendar.MONTH);

  private static final int YEAR_FUTURE = Calendar.getInstance().get(Calendar.YEAR) + 1;
  private static final int YEAR_PAST = Calendar.getInstance().get(Calendar.YEAR) - 2;
  private static final int MONTH_PAST = (MONTH > Calendar.getInstance().getMinimum(Calendar.MONTH)) ? MONTH - 1 : Calendar.getInstance().getMaximum(Calendar.MONTH);


  private static final String CVC_3 = "123";
  private static final String CVC_4 = "1234";

  private static final String MASTER_CARD_NUMBER = "5105105105105100";

  private static final String DISCOVER_CARD_NUMBER = "6500000000000002";

  private static final String VISA_CARD_NUMBER = "4111111111111111";
  private static final String VISA_CARD_NUMBER_2 = "4342-5611-1111-1118";

  private static final String AMEX_CARD_NUMBER = "341111111111111";


  // Calendar cal;

  @Before
  public void setup() {
//    cal = Calendar.getInstance();
//    cal.set(Calendar.YEAR, 2015);
//    cal.set(Calendar.MONTH, Calendar.MARCH);
//    cal.set(Calendar.DAY_OF_MONTH, 17);
  }

  @Test
  public void testExpiredCardMonth() throws Exception {
    Card card = new Card.Builder(MASTER_CARD_NUMBER, YEAR, MONTH_PAST, "123").build();
    assertSame(false, card.validExpiryDate());
  }

  @Test
  public void testExpiredCardYear() throws Exception {
    Card card = new Card.Builder(MASTER_CARD_NUMBER, YEAR_PAST, Calendar.DECEMBER + 1, "123").build();
    assertSame(false, card.validExpiryDate());
  }

  @Test
  public void canInitializeCardWithBuilder() throws Exception {
    Card card = new Card.Builder(MASTER_CARD_NUMBER, 3, YEAR_FUTURE, "123").build();
    assertTrue(card.isValid());
  }

  @Test
  public void testTypeDetectionMasterCard() throws Exception {
    Card card = new Card(MASTER_CARD_NUMBER, YEAR, MONTH, CVC_3);
    assertSame(Card.CardType.MASTERCARD, card.getType());
  }

  @Test
  public void testTypeDetectionAmericanExpress() throws Exception {
    Card card = new Card(AMEX_CARD_NUMBER, YEAR, MONTH, CVC_4);
    assertSame(Card.CardType.AMERICAN_EXPRESS, card.getType());
  }

  @Test
  public void testTypeDetectionVisaCard() throws Exception {
    Card card = new Card(VISA_CARD_NUMBER, YEAR, MONTH, CVC_3);
    assertSame(Card.CardType.VISA, card.getType());
  }

  @Test
  public void testTypeDetectionDiscoverCard() throws Exception {
    Card card = new Card(DISCOVER_CARD_NUMBER, YEAR, MONTH, CVC_3);
    assertSame(Card.CardType.DISCOVER, card.getType());
  }
}
