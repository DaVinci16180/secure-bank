package src.main.java.service;

import src.main.java.security.CryptographyService;
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
        boolean success = CryptographyService.verifyPassword(password.getValue(), account.getPassword().getValue());

        if (success)
            return database.saveSession(account);

        throw new IllegalArgumentException("Número da conta ou senha inválidos!");
    }

    public UUID authenticate(Account account, String password) {
        boolean success = CryptographyService.verifyPassword(password, account.getPassword().getValue());

        if (success)
            return database.saveSession(account);

        throw new IllegalArgumentException("Número da conta ou senha inválidos!");
    }

    public void endSession(UUID sessionId) {
        database.deleteSession(sessionId);
    }
}
