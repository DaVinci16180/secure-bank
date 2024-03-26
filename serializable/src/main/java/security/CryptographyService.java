package src.main.java.security;

import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.*;
import java.math.BigInteger;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptographyService {

    public static byte[] encryptAES(Object message, Key key) {
        try {
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream objOutStream = new ObjectOutputStream(byteOutStream);

            objOutStream.writeObject(message);
            byte[] arr = byteOutStream.toByteArray();

            objOutStream.close();
            byteOutStream.close();

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encode(cipher.doFinal(arr));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object decryptAES(byte[] cipherText, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] message = cipher.doFinal(Base64.getDecoder().decode(cipherText));

            ByteArrayInputStream byteInStream = new ByteArrayInputStream(message);
            ObjectInput objectInput = new ObjectInputStream(byteInStream);

            Object result = objectInput.readObject();
            objectInput.close();

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String sign(Object message, Key key) {
        try {
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream objOutStream = new ObjectOutputStream(byteOutStream);

            objOutStream.writeObject(message);
            byte[] arr = byteOutStream.toByteArray();

            objOutStream.close();
            byteOutStream.close();

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(key);

            return Base64.getEncoder().encodeToString(mac.doFinal(arr));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static SecretKey generateKey(String algorithm) {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(algorithm);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static CustomKeyPair generateKeyPair() {
        SecureRandom random = new SecureRandom();

        BigInteger p = new BigInteger(512, 100, random);
        BigInteger q = new BigInteger(512, 100, random);
        BigInteger mod = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger pub = new BigInteger(512, 100, random);
        while (phi.gcd(pub).intValue() > 1)
            pub = pub.add(new BigInteger("2"));

        CustomKey publicKey = new CustomKey(pub, mod);
        CustomKey privateKey = new CustomKey(pub.modInverse(phi), mod);

        return new CustomKeyPair(privateKey, publicKey);
    }

    public static byte[] encryptRSA(byte[] message, CustomKey publicKey) {
        try {
            return new BigInteger(message).modPow(publicKey.value(), publicKey.mod()).toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decryptRSA(byte[] cipherText, CustomKey privateKey) {
        try {
            return new BigInteger(cipherText).modPow(privateKey.value(), privateKey.mod()).toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptAESKey(SecretKey key, CustomKey rsaPublicKey) {
        return encryptRSA(Base64.getEncoder().encode(key.getEncoded()), rsaPublicKey);
    }

    public static SecretKey decryptAESKey(byte[] cipher, CustomKey rsaPrivateKey) {
        byte[] decodedKey = Base64.getDecoder().decode(decryptRSA(cipher, rsaPrivateKey));
        return new javax.crypto.spec.SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}