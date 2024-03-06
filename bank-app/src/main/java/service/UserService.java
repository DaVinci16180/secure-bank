package src.main.java.service;

import src.main.java.CryptographyService;
import src.main.java.database.Database;
import src.main.java.model.Account;
import src.main.java.model.Password;

import java.util.UUID;

public class UserService {

    private static final class InstanceHolder {
        private static final UserService instance = new UserService();
    }

    public static UserService getInstance() {

        return UserService.InstanceHolder.instance;
    }

    private UserService() {}

    Database database = Database.getInstance();

    public UUID authenticate(String accountNumber, Password password) {
        Account account = database.findAccount(UUID.fromString(accountNumber)).orElseThrow();
        return authenticate(account, password);
    }

    public UUID authenticate(Account account, Password password) {
        String encryptedPassword;
        if (!password.isEncrypted())
            encryptedPassword = CryptographyService.encryptPlayfair(password.getValue());
        else
            encryptedPassword = password.getValue();

        boolean success = account.getPassword().getValue().equals(encryptedPassword);

        if (success)
            return database.saveSession(account);

        throw new IllegalArgumentException("Número da conta ou senha inválidos!");
    }

    public void endSession(UUID sessionId) {
        database.deleteSession(sessionId);
    }
}
