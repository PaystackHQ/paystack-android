## Paystack Android

This is a library for easy integration of [Paystack](https://paystack.com) with your Android application. Use this
library in your android app so we shoulder the burden of PCI compliance by helping you
avoid the need to send card data directly to your server. Instead, this library sends credit
card data directly to our servers.

## Requirements
- Android SDKv16 (Android 4.1 "Jelly Bean") - This is the first SDK version that includes
`TLSv1.2` which is required by our servers. Native app support for user devices older than
API 16 will not be available.

## Installation

### Android Studio (using Gradle)
You do not need to clone this repository or download the files. Just add the following lines to your app's `build.gradle`:

```gradle
repositories {
  maven {
      url 'https://dl.bintray.com/paystack/maven/'
  }
}
dependencies {
  compile 'co.paystack.android:paystack:2.0'
}
```

### Eclipse
To use this library with Eclipse, you need to:

1. Clone the repository.
2. Import the **Paystack** folder into your [Eclipse](http://help.eclipse.org/juno/topic/org.eclipse.platform.doc.user/tasks/tasks-importproject.htm) project
3. In your project settings, add the **Paystack** project under the Libraries section of the Android category.

## Usage

### 0. Prepare for use

To prepare for use, you must ensure that your app has internet permissions by making sure the `uses-permission` line below is present in the AndroidManifest.xml.

```xml
<manifest xlmns:android...>
 ...
 <uses-permission android:name="android.permission.INTERNET" />
 <application ... />
</manifest>
```

### 1. initializeSdk

To use the Paysack android sdk, you need to first initialize the sdk using the PaystackSdk class.

```java
PaystackSdk.initialize(getApplicationContext());
```

Make sure to call this method in the onCreate method of your Fragment or Activity.

### 2. setPublicKey

Before you can create a token with the PaystackSdk, you need to set your public key. The library provides two approaches,

#### a. Add the following lines to the `<application></application>` tag of your AndroidManifest.xml

```xml
<meta-data
    android:name="co.paystack.android.PublicKey"
    android:value="your public key obtained from: https://dashboard.paystack.co/#/settings/developer"/>
```

#### b. Set the public key by code

```java
PaystackSdk.setPublicKey(publicKey);
```

### 3. chargeCard
Charging with the PaystackSdk is quite straightforward.
```java
    //create a charge
    Charge charge = new Charge();
    //Add card to the charge
    charge.setCard(new Card.Builder(cardNumber, expiryMonth, expiryYear, cvc).build());
    //add an email for customer
    charge.setEmail(email);
    //add amount to charge
    charge.setAmount(amount);

    //charge card
    PaystackSdk.chargeCard(activity, charge, new Paystack.TransactionCallback() {
        @Override
        public void onSuccess(Transaction transaction) {
            // This is called only after transaction is deemed successful
            // retrieve the transaction, and send its reference to your server
            // for verification.
        }

        @Override
        public void beforeValidate(Transaction transaction) {
            // This is called only before requesting OTP
            // Save reference so you may send to server. If
            // error occurs with OTP, you should still verify on server
        }

        @Override
        public void onError(Throwable error) {
          //handle error here
        }

    });
```
The first argument to the PaystackSdk.chargeCard is the calling activity object. Always
give an activity that will stay open till the end of the transaction. The currently
open activity is just fine.

### 4. Verifying the transaction
Send the reference to your server and verify by calling our REST API. An authorization will be returned which
will allow you know if its code is reusable. You can learn more about our verify call [here](developers.paystack.co/docs/verifying-transactions).


> We are retiring the createToken function. You can access the old documentation [here](CREATETOKEN.md).


### 5. Charging Returning Customers
See details for charging returning customers [here](https://developers.paystack.co/docs/charging-returning-customers).

### 6. Library aided card validation & utility methods
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
3. Add your public key to your AndroidManifest.xml file
4. Build and run the project on your device or emulator

## Contact

For more enquiries and technical questions regarding the Android PaystackSdk, please send an email to support@paystack.com
