package co.paystack.android.utils;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import co.paystack.android.exceptions.AuthenticationException;


/**
 * Class for encrypting the card details, for token creation.
 *
 * @author {androidsupport@paystack.co} on 8/10/15.
 */
public class Crypto {
    private static final String PAYSTACK_RSA_PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALhZs/7hP0g0+hrqTq0hFyGVxgco0NMxZD8nPS6ihxap0yNFjzdyUuZED6P4/aK9Ezl5ajEI9pcx5/1BrEE+F3kCAwEAAQ==";
    private static String ALGORITHM = "RSA";
    private static String CIPHER = "RSA/ECB/PKCS1Padding";

    private static byte[] encrypt(String text, PublicKey key) {
    byte[] cipherText = null;

    try {

      // get an RSA cipher object
      final Cipher cipher = Cipher.getInstance(CIPHER);

      //init cipher and encrypt the plain text using the public key
      cipher.init(Cipher.ENCRYPT_MODE, key);
      cipherText = cipher.doFinal(text.getBytes());

    } catch (Exception e) {

      e.printStackTrace();
    }
    return cipherText;
  }

  public static String encrypt(String text, String publicKey) throws AuthenticationException {
    return new String(Base64.encode(encrypt(text, getPublicKeyFromString(publicKey)), Base64.NO_WRAP));
  }

    public static String encrypt(String text) throws AuthenticationException {
        return new String(Base64.encode(encrypt(text, getPublicKeyFromString(PAYSTACK_RSA_PUBLIC_KEY)), Base64.NO_WRAP));
    }

    private static String decrypt(byte[] text, PrivateKey key) {
    byte[] decryptedText = null;

    try {
      // get an RSA cipher object
      final Cipher cipher = Cipher.getInstance(CIPHER);

      //init cipher and decrypt the text using the private key
      cipher.init(Cipher.DECRYPT_MODE, key);
      decryptedText = cipher.doFinal(text);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return new String((decryptedText != null) ? decryptedText : new byte[0]);
  }

  private static PublicKey getPublicKeyFromString(String pubKey) throws AuthenticationException {

    PublicKey key;

    try {
      //init keyfactory
      KeyFactory kf = KeyFactory.getInstance(ALGORITHM);

      //decode the key into a byte array
      byte[] keyBytes = Base64.decode(pubKey, Base64.NO_WRAP);

      //create spec
      X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);

      //generate public key
      key = kf.generatePublic(spec);
    } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
        throw new AuthenticationException("Invalid public key: " + e.getMessage());
    }
    return key;


  }
}
