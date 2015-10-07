## paystack-android

This is a library for easy integration of Paystack with your Android application

## Installation

### Android Studio (using Gradle)
You do not need to clone this repository or download the files. Just add the following lines to your app's `build.gradle`:

    repositories {
      maven {
          url 'https://dl.bintray.com/paystack/maven/'
      }
    }
    dependencies {
      compile 'co.paystack.android:paystack:1.0.0'
    }

### Eclipse
To use this library with Eclipse, you need to:

1. Clone the repository.
2. Import the **Paystack** folder into your [Eclipse](http://help.eclipse.org/juno/topic/org.eclipse.platform.doc.user/tasks/tasks-importproject.htm) project
3. In your project settings, add the **Paystack** project under the Libraries section of the Android category.

## Usage

### 1. initializeSdk

To use the Paysack android sdk, you need to first initialize the sdk using the PaystackSdk class.

    PaystackSdk.initialize(getApplicationContext());

Make sure to call this method in the onCreate method of your Fragment or Activity.

### 2. setPublishableKey

Before you can create a token with the PaystackSdk, you need to set your publishable key. The library provides two approaches,

#### a. Add the following lines to the `<application></application>` tag of your AndroidManifest.xml
    <meta-data
        android:name="co.paystack.android.PublishableKey"
        android:value="your publishable key"/>

#### b. Set the publishable key by code
    PaystackSdk.setPublishableKey(publishableKey);


### 3. createToken
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

### 4. Library aided card validation & utility methods
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
3. Add your publishable key to your AndroidManifest.xml file
4. Build and run the project on your device or emulator
