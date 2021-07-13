package co.paystack.android.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import co.paystack.android.utils.CardUtils;
import co.paystack.android.utils.StringUtils;

/**
 * The class for the card model. Has utility methods for validating the card.\n
 * Best initialized with the Card.Builder class
 *
 * @author {androidsupport@paystack.co} on 8/10/15.
 */
public class Card extends PaystackModel implements Serializable {


    //declare fields in a typical card
    /**
     * List of cardTypes
     */
    private static final List<CardType> cardTypes = Arrays.asList(
            new Visa(),
            new MasterCard(),
            new AmericanExpress(),
            new DinersClub(),
            new Jcb(),
            new Verve(),
            new Discover()
    );
    /**
     * Name on the card
     */
    private String name;
    /**
     * Card number
     */
    private String number;
    /**
     * CVC number
     */
    private String cvc;
    /**
     * Expiry month
     */
    private Integer expiryMonth;
    /**
     * Expiry year
     */
    private Integer expiryYear;
    /**
     * Bank Address line 1-4
     */
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String addressLine4;
    /**
     * Postal code of the bank address
     */
    private String addressPostalCode;
    /**
     * Country of the bank
     */
    private String addressCountry;
    private String country;
    /**
     * Type of card
     */
    private String type;
    private String last4digits;

    /**
     * Private constructor for a Card object, using a Builder;
     *
     * @param builder - a builder with which to build the card object
     */
    private Card(Builder builder) {
        this.number = StringUtils.nullify(builder.number);
        this.expiryMonth = builder.expiryMonth;
        this.expiryYear = builder.expiryYear;
        this.cvc = StringUtils.nullify(builder.cvc);

        this.name = StringUtils.nullify(builder.name);
        this.addressLine1 = StringUtils.nullify(builder.addressLine1);
        this.addressLine2 = StringUtils.nullify(builder.addressLine2);
        this.addressLine3 = StringUtils.nullify(builder.addressLine3);
        this.addressLine4 = StringUtils.nullify(builder.addressLine4);
        this.addressCountry = StringUtils.nullify(builder.addressCountry);
        this.addressPostalCode = StringUtils.nullify(builder.addressPostalCode);

        this.country = StringUtils.nullify(builder.country);
        this.type = getType();
        this.last4digits = builder.last4digits;
    }

    /**
     * Public constructor for a Card object using field values:
     *
     * @param number            - card number
     * @param expiryMonth       - card expiry month
     * @param expiryYear        - card expiry year
     * @param cvc               - card CVC
     * @param name              - card owner's name
     * @param addressLine1      - address line 1
     * @param addressLine2      - address line 2
     * @param addressLine3      - address line 3
     * @param addressLine4      - address line 4
     * @param addressCountry    - address country
     * @param addressPostalCode - address postal code
     * @param country           - card country
     */
    public Card(String number, Integer expiryMonth, Integer expiryYear, String cvc, String name,
                String addressLine1, String addressLine2, String addressLine3, String addressLine4,
                String addressCountry, String addressPostalCode, String country) {
        // use builder to set first 5 required fields
        Builder intermediate = new Builder(number, expiryMonth, expiryYear, cvc);
        this.number = intermediate.number;
        this.expiryMonth = intermediate.expiryMonth;
        this.expiryYear = intermediate.expiryYear;
        this.cvc = intermediate.cvc;
        this.last4digits = intermediate.last4digits;

        this.name = StringUtils.nullify(name);
        this.addressLine1 = StringUtils.nullify(addressLine1);
        this.addressLine2 = StringUtils.nullify(addressLine2);
        this.addressLine3 = StringUtils.nullify(addressLine3);
        this.addressLine4 = StringUtils.nullify(addressLine4);
        this.addressCountry = StringUtils.nullify(addressCountry);
        this.addressPostalCode = StringUtils.nullify(addressPostalCode);

        this.country = StringUtils.nullify(country);

        this.type = getType();
    }

    /**
     * Public constructor for a Card object using field values:
     *
     * @param number            - card number
     * @param expiryMonth       - card expiry month
     * @param expiryYear        - card expiry year
     * @param cvc               - card CVC
     * @param name              - card owner's name
     * @param addressLine1      - address line 1
     * @param addressLine2      - address line 2
     * @param addressLine3      - address line 3
     * @param addressLine4      - address line 4
     * @param addressCountry    - address country
     * @param addressPostalCode - address postal code
     */
    public Card(String number, Integer expiryMonth, Integer expiryYear, String cvc, String name,
                String addressLine1, String addressLine2, String addressLine3, String addressLine4,
                String addressCountry, String addressPostalCode) {
        this(number, expiryMonth, expiryYear, cvc, name, addressLine1, addressLine2, addressLine3,
                addressLine4, addressCountry, addressPostalCode, null);
    }

    /**
     * Public constructor for a Card object using field values:
     *
     * @param number      - card number
     * @param expiryMonth - card expiry month
     * @param expiryYear  - card expiry year
     * @param cvc         - card CVC
     * @param name        - card owner's name
     */
    public Card(String number, Integer expiryMonth, Integer expiryYear, String cvc, String name) {
        this(number, expiryMonth, expiryYear, cvc, name, null, null, null,
                null, null, null);
    }

    /**
     * Public constructor for a Card object using field values:
     *
     * @param number      - card number
     * @param expiryMonth - card expiry month
     * @param expiryYear  - card expiry year
     * @param cvc         - card CVC
     */
    public Card(String number, Integer expiryMonth, Integer expiryYear, String cvc) {
        this(number, expiryMonth, expiryYear, cvc, null, null, null, null,
                null, null, null);
    }

    /**
     * Validates the Card object
     *
     * @return True if card is valid, false otherwise
     */
    public boolean isValid() {
        if (cvc == null) {
            return false;
        } else if (number == null) {
            return false;
        } else if (expiryMonth == null) {
            return false;
        } else if (expiryYear == null) {
            return false;
        } else {
            return validNumber() && validExpiryDate() && validCVC();
        }
    }

    /**
     * Method that validates the CVC or CVV of the card object
     *
     * @return true if the cvc is valid
     */
    public boolean validCVC() {
        //validate cvc
        if (StringUtils.isEmpty(cvc)) {
            return false;
        }
        String cvcValue = cvc.trim();

        boolean validLength = ((type == null && cvcValue.length() >= 3 && cvcValue.length() <= 4) ||
                (CardType.AMERICAN_EXPRESS.equals(type) && cvcValue.length() == 4) ||
                (!CardType.AMERICAN_EXPRESS.equals(type) && cvcValue.length() == 3));

        return !(!CardUtils.isWholePositiveNumber(cvcValue) || !validLength);
    }

    /**
     * Method that validates the card number of the card object
     *
     * @return true if the card number is valid
     */
    public boolean validNumber() {
        if (StringUtils.isEmpty(number)) return false;

        //remove all non 0-9 characters
        String formattedNumber = number.trim().replaceAll("[^0-9]", "");

        // Verve card needs no other validation except that it matched pattern
        if (formattedNumber.matches(CardType.PATTERN_VERVE)) {
            return true;
        }

        //check if formattedNumber is empty or card isn't a whole positive number or isn't Luhn-valid
        if (StringUtils.isEmpty(formattedNumber)
                || !CardUtils.isWholePositiveNumber(number)
                || !isValidLuhnNumber(number)) {
            return false;
        }

        //check type for lengths
        if (CardType.AMERICAN_EXPRESS.equals(type)) {
            return formattedNumber.length() == CardType.MAX_LENGTH_AMERICAN_EXPRESS;
        } else if (CardType.DINERS_CLUB.equals(type)) {
            return formattedNumber.length() == CardType.MAX_LENGTH_DINERS_CLUB;
        } else {
            return formattedNumber.length() == CardType.MAX_LENGTH_NORMAL;
        }
    }

    /**
     * Validates the number against Luhn algorithm https://de.wikipedia.org/wiki/Luhn-Algorithmus#Java
     *
     * @param number - number to validate
     * @return true if the number is Luhn-valid
     */
    private boolean isValidLuhnNumber(String number) {
        int sum = 0;
        int length = number.trim().length();

        //iterate through from the rear
        for (int i = 0; i < length; i++) {
            char c = number.charAt(length - 1 - i);

            //check if character is a digit before parsing it
            if (!Character.isDigit(c)) {
                return false;
            }

            int digit = Integer.parseInt(c + "");

            //if it's odd, multiply by 2
            if (i % 2 == 1)
                digit *= 2;

            sum += digit > 9 ? digit - 9 : digit;
        }
        return sum % 10 == 0;
    }

    /**
     * Method that validates the expiry date of the card
     *
     * @return true if the expiry date is valid or hasn't been passed, false otherwise
     */
    public boolean validExpiryDate() {
        //validate month and year
        return !(expiryMonth == null || expiryYear == null) && CardUtils.isNotExpired(expiryYear, expiryMonth) && CardUtils.isValidMonth(expiryMonth);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = StringUtils.normalizeCardNumber(number);
        this.last4digits = CardUtils.retrieveLast4Digits(number);
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public Integer getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(Integer expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public Integer getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(Integer expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getAddressLine4() {
        return addressLine4;
    }

    public void setAddressLine4(String addressLine4) {
        this.addressLine4 = addressLine4;
    }

    public String getAddressPostalCode() {
        return addressPostalCode;
    }

    public void setAddressPostalCode(String addressPostalCode) {
        this.addressPostalCode = addressPostalCode;
    }

    public String getAddressCountry() {
        return addressCountry;
    }

    public void setAddressCountry(String addressCountry) {
        this.addressCountry = addressCountry;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Method that returns the type of the card
     *
     * @return a String representation of the card type detected. You can use this to improve the experience on your form,
     * to display the card type logo, while the user is entering the card number e.g MasterCard, Discover, Visa etc.
     */
    public String getType() {
        //if type is empty and the number isn't empty
        if (StringUtils.isEmpty(type) && !StringUtils.isEmpty(number)) {
            for (CardType cardType : cardTypes) {
                if (cardType.matches(number)) {
                    return cardType.toString();
                }
            }
            return CardType.UNKNOWN;
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLast4digits() {
        return last4digits;
    }

    /**
     * Builder class to build a card;
     */
    public static class Builder {
        private String name;
        private String number;
        private String cvc;
        private Integer expiryMonth;
        private Integer expiryYear;
        private String addressLine1;
        private String addressLine2;
        private String addressLine3;
        private String addressLine4;
        private String addressPostalCode;
        private String addressCountry;
        private String country;
        private String type;
        private String last4digits;

        /**
         * Public constructor for the builder, with important fields set
         *
         * @param number      - Card number
         * @param expiryMonth - card Expiry Month
         * @param expiryYear  - card Expiry Year
         * @param cvc         - Card CVC
         */
        public Builder(String number, Integer expiryMonth, Integer expiryYear, String cvc) {
            this.setNumber(number);
            this.expiryMonth = expiryMonth;
            this.expiryYear = expiryYear;
            this.cvc = cvc;
        }

        public Builder setNumber(String number) {
            this.number = StringUtils.normalizeCardNumber(number);
            last4digits = CardUtils.retrieveLast4Digits(number);
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
            return this;
        }

        public Builder setAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
            return this;
        }

        public Builder setAddressLine3(String addressLine3) {
            this.addressLine3 = addressLine3;
            return this;
        }

        public Builder setAddressLine4(String addressLine4) {
            this.addressLine4 = addressLine4;
            return this;
        }

        public Builder setAddressCountry(String addressCountry) {
            this.addressCountry = addressCountry;
            return this;
        }

        public Builder setAddressPostalCode(String addressPostalCode) {
            this.addressPostalCode = addressPostalCode;
            return this;
        }


        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }


        public Card build() {
            return new Card(this);
        }
    }

    public static abstract class CardType {
        //card types
        public static final String VISA = "Visa";
        public static final String MASTERCARD = "MasterCard";
        public static final String AMERICAN_EXPRESS = "American Express";
        public static final String DINERS_CLUB = "Diners Club";
        public static final String DISCOVER = "Discover";
        public static final String JCB = "JCB";
        public static final String VERVE = "VERVE";
        public static final String UNKNOWN = "Unknown";
        //lengths for some cards
        public static final int MAX_LENGTH_NORMAL = 16;
        public static final int MAX_LENGTH_AMERICAN_EXPRESS = 15;
        public static final int MAX_LENGTH_DINERS_CLUB = 14;
        //source of these regex patterns http://stackoverflow.com/questions/72768/how-do-you-detect-credit-card-type-based-on-number
        public static final String PATTERN_VISA = "^4[0-9]{12}(?:[0-9]{3})?$";
        public static final String PATTERN_MASTERCARD = "^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$";
        public static final String PATTERN_AMERICAN_EXPRESS = "^3[47][0-9]{13}$";
        public static final String PATTERN_DINERS_CLUB = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
        public static final String PATTERN_DISCOVER = "^6(?:011|5[0-9]{2})[0-9]{12}$";
        public static final String PATTERN_JCB = "^(?:2131|1800|35[0-9]{3})[0-9]{11}$";
        public static final String PATTERN_VERVE = "^((506(0|1))|(507(8|9))|(6500))[0-9]{12,15}$";

        public abstract boolean matches(String card);

        @Override
        public abstract String toString();
    }

    /**
     * Private clsas for VISA card
     */
    private static class Visa extends CardType {
        @Override
        public boolean matches(String card) {
            return card.matches(PATTERN_VISA);
        }

        @Override
        public String toString() {
            return VISA;
        }
    }

    private static class MasterCard extends CardType {
        @Override
        public boolean matches(String card) {
            return card.matches(PATTERN_MASTERCARD);
        }

        @Override
        public String toString() {
            return MASTERCARD;
        }
    }

    private static class AmericanExpress extends CardType {
        @Override
        public boolean matches(String card) {
            return card.matches(PATTERN_AMERICAN_EXPRESS);
        }

        @Override
        public String toString() {
            return AMERICAN_EXPRESS;
        }
    }

    private static class DinersClub extends CardType {
        @Override
        public boolean matches(String card) {
            return card.matches(PATTERN_DINERS_CLUB);
        }

        @Override
        public String toString() {
            return DINERS_CLUB;
        }
    }

    private static class Discover extends CardType {

        @Override
        public boolean matches(String card) {
            return card.matches(PATTERN_DISCOVER);
        }

        @Override
        public String toString() {
            return DISCOVER;
        }
    }

    private static class Jcb extends CardType {

        @Override
        public boolean matches(String card) {
            return card.matches(PATTERN_JCB);
        }

        @Override
        public String toString() {
            return JCB;
        }
    }

    private static class Verve extends CardType {

        @Override
        public boolean matches(String card) {
            return card.matches(PATTERN_VERVE);
        }

        @Override
        public String toString() {
            return VERVE;
        }
    }
}
