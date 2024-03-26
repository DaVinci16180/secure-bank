package main.java.network;

import src.main.java.security.CryptographyService;
import src.main.java.security.CustomKey;
import src.main.java.security.CustomKeyPair;

import java.security.Key;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.UUID;

public class LocalStorage {

    private static final class InstanceHolder {
        private static final LocalStorage instance = new LocalStorage();
    }

    public static LocalStorage getInstance() {

        return LocalStorage.InstanceHolder.instance;
    }

    private LocalStorage() {}

    public CustomKeyPair keyPair = CryptographyService.generateKeyPair();
    public Key hmac;
    public CustomKey serverPublicKey;
    public UUID sessionId = null;
    public String accountNumber;
    public String userName;
    public boolean error = false;
    public String errorMessage;

    public void clearSession() {
        this.sessionId = null;
        this.userName = null;
        this.accountNumber = null;
    }

    public void resetError() {
        error = false;
        errorMessage = "";
    }

    public void reset() {
        clearSession();
        serverPublicKey = null;
    }

}
