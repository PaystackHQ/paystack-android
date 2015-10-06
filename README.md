## paystack-android

## Installation

### Android Studio (using Gradle)
You do not need to clone this repository or download the files. Just add this line to your `build.gradle` file within the `dependecy` section:

    compile 'co.paystack:paystack-android:+'

### Eclipse
To use this library with Eclipse, you need to:

1. Clone the repository.
2. Import the *Paystack* folder into your [Eclipse](http://help.eclipse.org/juno/topic/org.eclipse.platform.doc.user/tasks/tasks-importproject.htm) project
3. In your project settings, add the *Paystack* project under the Libraries section of the Android category.

## Usage

### initializeSdk

To use the Paysack android sdk, you need to first initialize the sdk using the PaystackSdk class.

    PaystackSdk.initialize(getApplicationContext());

### createToken
Creating a token with the PaystackSdk is quite straightforward.

    //build a card
    Card card = new Card.Builder(cardNumber, expiryMonth, expiryYear, cvc).build();

    //create token
    PaystackSdk.createToken(card, new Paystack.TokenCallback() {
        @Override
        public void onCreate(Token token) {
            //retrieve the token, and send to your server for charging.
        }

        @Override
        public void onError(Exception error) {
            //handle error here
        }
    });

The first argument to the PaystackSdk.createToken is the card object. A basic Card object contains:

+ cardNumber: the card number as a String without any seperator e.g 5555555555554444
+ expiryMonth: the expiry month as an integer ranging from 1-12 e.g 10 (October)
+ expiryYear: the expiry year as an integer e.g 2015
+ cvc: the card security code as a String e.g 123

### Library aided card validation & utility methods
The library provides validation methods to validate the fields of the card.

#### card.validNumber
This method helps to perform a check if the card number is valid.

#### card.validCVC
Method that checks if the card security code is valid

#### card.validExpiryDate
Method checks if the expiry date (combination of year and month) is valid.

#### card.isValid
Method to check if the card is valid. Always do this check, before creating the token.

#### card.getType
This method returns an estimate of the string representation of the card type.

## Building the example project

1. Clone the repository.
2. Import the project either using Android Studio or Eclipse
3. Build and run the project on your device or emulator
