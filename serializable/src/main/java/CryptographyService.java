package src.main.java;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import java.io.*;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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

    public static Object decryptAES(byte[] hash, Key key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] message = cipher.doFinal(Base64.getDecoder().decode(hash));

            ByteArrayInputStream byteInStream = new ByteArrayInputStream(message);
            ObjectInput objectInput = new ObjectInputStream(byteInStream);

            Object result = objectInput.readObject();
            objectInput.close();

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] encryptRSA(Object message, PublicKey publicKey) {
        Cipher cipher;
        try {
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream objOutStream = new ObjectOutputStream(byteOutStream);

            objOutStream.writeObject(message);
            byte[] arr = byteOutStream.toByteArray();

            objOutStream.close();
            byteOutStream.close();

            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(arr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object decryptRSA(byte[] hash, PrivateKey privateKey) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] message = cipher.doFinal(hash);

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

    public static Key generateKey(String algorithm) {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance(algorithm);
//            keyGenerator.init(128);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static KeyPair generateKeyPair() {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    // CIFRA DE PLAYFAIR COM LETRAS E NUMEROS

    private static final char[][] MATRIX = generateMatrix("chave123");

    private static char[][] generateMatrix(String key) {
        List<Character> chars = new ArrayList<>();
        for (char c = 48; c <=57; c++)
            chars.add(c);

        for (char c = 97; c <=122; c++)
            chars.add(c);

        chars.remove(Character.valueOf('j'));
        char[][] matrix = new char[7][5];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 5; j++) {
                int keyIterator = i * 5 + j;
                if (key.length() >= keyIterator + 1)
                    matrix[i][j] = key.charAt(keyIterator);
                else
                    matrix[i][j] = chars.get(0);

                chars.remove(Character.valueOf(matrix[i][j]));
            }
        }

        return matrix;
    }

    public static String encryptPlayfair(String plaintext) {
        StringBuilder encryptedText = new StringBuilder();
        plaintext = plaintext.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        for (int i = 0; i < plaintext.length(); i += 2) {
            char firstChar = plaintext.charAt(i);
            char secondChar = (i + 1 < plaintext.length()) ? plaintext.charAt(i + 1) : 'x';
            int[] firstPos = getPosition(firstChar);
            int[] secondPos = getPosition(secondChar);

            if (firstPos[0] == secondPos[0]) {
                encryptedText.append(MATRIX[firstPos[0]][(firstPos[1] + 1) % 5]);
                encryptedText.append(MATRIX[secondPos[0]][(secondPos[1] + 1) % 5]);
            } else if (firstPos[1] == secondPos[1]) {
                encryptedText.append(MATRIX[(firstPos[0] + 1) % 7][firstPos[1]]);
                encryptedText.append(MATRIX[(secondPos[0] + 1) % 7][secondPos[1]]);
            } else {
                encryptedText.append(MATRIX[firstPos[0]][secondPos[1]]);
                encryptedText.append(MATRIX[secondPos[0]][firstPos[1]]);
            }
        }

        return encryptedText.toString();
    }

    public static String decryptPlayfair(String ciphertext) {
        StringBuilder decryptedText = new StringBuilder();

        for (int i = 0; i < ciphertext.length(); i += 2) {
            char firstChar = ciphertext.charAt(i);
            char secondChar = ciphertext.charAt(i + 1);
            int[] firstPos = getPosition(firstChar);
            int[] secondPos = getPosition(secondChar);

            if (firstPos[0] == secondPos[0]) {
                decryptedText.append(MATRIX[firstPos[0]][(firstPos[1] + 4) % 5]);
                decryptedText.append(MATRIX[secondPos[0]][(secondPos[1] + 4) % 5]);
            } else if (firstPos[1] == secondPos[1]) {
                decryptedText.append(MATRIX[(firstPos[0] + 5) % 7][firstPos[1]]);
                decryptedText.append(MATRIX[(secondPos[0] + 5) % 7][secondPos[1]]);
            } else {
                decryptedText.append(MATRIX[firstPos[0]][secondPos[1]]);
                decryptedText.append(MATRIX[secondPos[0]][firstPos[1]]);
            }
        }

        return decryptedText.toString();
    }

    private static int[] getPosition(char letter) {
        for (int i = 0; i < MATRIX.length; i++) {
            for (int j = 0; j < MATRIX[i].length; j++) {
                if (MATRIX[i][j] == letter) {
                    return new int[]{i, j};
                }
            }
        }

        return new int[]{-1, -1};
    }
}
