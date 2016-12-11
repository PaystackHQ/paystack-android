### createToken
Creating a single-use token with the PaystackSdk is quite straightforward.
```java
class tokeningActivity{
    public void createToken(){
        //build a card
        Card card = new Card.Builder(cardNumber, expiryMonth, expiryYear, cvc).build();

        //create token
        PaystackSdk.createToken(card, new Paystack.TokenCallback() {
            @Override
            public void onCreate(Token token) {
                //retrieve the token, and send to your server for charging.
            }

            @Override
            public void onError(Throwable error) {
                //handle error here
            }
        });
    }
}
```
The first argument to the PaystackSdk.createToken is the card object. A basic Card object contains:

+ cardNumber: the card number as a String without any seperator e.g 5555555555554444
+ expiryMonth: the expiry month as an integer ranging from 1-12 e.g 10 (October)
+ expiryYear: the expiry year as an integer e.g 2015
+ cvc: the card security code as a String e.g 123

### Charging the tokens.
Send the token to your server and create a charge by calling our REST API. An authorization_code will be returned once the single-use token has been charged successfully. You can learn more about our API [here](https://developers.paystack.co/docs/getting-started).

 **Endpoint:** https://api.paystack.co/transaction/charge_token

 **Parameters:**


 - email  - customer's email address (required)
 - reference - unique reference  (required)
 - amount - Amount in Kobo (required)

**Example**

```bash
   $ curl https://api.paystack.co/transaction/charge_token \
    -H "Authorization: Bearer SECRET_KEY" \
    -H "Content-Type: application/json" \
    -d '{"token": "PSTK_r4ec2m75mrgsd8n9", "email": "customer@email.com", "amount": 10000, "reference": "amutaJHSYGWakinlade256"}' \
    -X POST

```
### Using the [Paystack-PHP library](https://github.com/yabacon/paystack-php) or [Paystack PHP class](https://github.com/yabacon/paystack-class)
```php
list($headers, $body, $code) = $paystack->transaction->chargeToken([
                'reference'=>'amutaJHSYGWakinlade256',
                'token'=>'PSTK_r4ec2m75mrgsd8n9',
                'email'=>'customer@email.com',
                'amount'=>10000 // in kobo
              ]);

// check if authorization code was generated
if ((intval($code) === 200) && array_key_exists('status', $body) && $body['status']) {
    // body contains Array with data similar to result below
    $authorization_code = $body['authorization']['authorization_code'];
    // save the authorization_code so you may charge in future

} else {
// invalid body was returned
// handle this or troubleshoot
    throw new \Exception('Transaction Initialise returned non-true status');
}

```


**Result**

```json
    {  
        "status": true,
        "message": "Charge successful",
        "data": {
            "amount": 10000,
            "transaction_date": "2016-01-26T15:34:02.000Z",
            "status": "success",
            "reference": "amutaJHSYGWakinlade256",
            "domain": "test",
            "authorization": {
            "authorization_code": "AUTH_d47nbp3x",
            "card_type": "visa",
            "last4": "1111",
            "bank": null
        },
        "customer": {
            "first_name": "John",
            "last_name": "Doe",
            "email": "customer@email.com"
        },
        "plan": 0
    }
```
