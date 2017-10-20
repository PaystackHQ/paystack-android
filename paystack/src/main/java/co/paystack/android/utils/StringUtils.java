package co.paystack.android.utils;

import co.paystack.android.exceptions.CardException;
import co.paystack.android.model.Card;

/**
 * String utility methods
 *
 * @author {androidsupport@paystack.co} on 9/13/15.
 */
public class StringUtils {

    public static final String CARD_CONCATENATOR = "*";

    public static boolean isEmpty(String value) {
    return value == null || value.length() < 1 || value.equalsIgnoreCase("null");
  }

  public static boolean isEmpty(CharSequence charSequence) {
    return charSequence == null || isEmpty(charSequence.toString());
  }

  /**
   * Method to nullify an empty String.
   *
   * @param value - A string we want to be sure to keep null if empty
   * @return null if a value is empty or null, otherwise, returns the value
   */
  public static String nullify(String value) {
    if (isEmpty(value)) {
      return null;
    }
    return value;
  }

    public static String concatenateCardFields(Card card) throws CardException {
        if (card == null) {
            throw new CardException("Card cannot be null");
        }

        String number = nullify(card.getNumber());
        String cvc = nullify(card.getCvc());
        int expiryMonth = card.getExpiryMonth();
        int expiryYear = card.getExpiryYear();

        String cardString = null;
        String[] cardFields = {number, cvc, expiryMonth + "", expiryYear + ""};

        if (!isEmpty(number)) {
            for (int i = 0; i < cardFields.length; i++) {
                if (i == 0 && cardFields.length > 1)
                    cardString = cardFields[i] + CARD_CONCATENATOR;
                else if (i == cardFields.length - 1)
                    cardString += cardFields[i];
                else
                    cardString = cardString + cardFields[i] + CARD_CONCATENATOR;
            }
            return cardString;
        } else {
            throw new CardException("Invalid card details: Card number is empty or null");
        }
    }


    /**
     * Method to remove non-numeric characters from card number
     * @param card_number - String of card number
     * @return card number with numeric characters only
     */
    public static String normalizeCardNumber(String card_number) {
        return card_number.replaceAll( "[^\\d]", "" );
    }
}
