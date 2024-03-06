package src.main.java.service;

import src.main.java.CryptographyService;
import src.main.java.database.Database;
import src.main.java.model.Account;
import src.main.java.model.Password;
import src.main.java.model.User;

import java.math.BigDecimal;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountService {

    private static final class InstanceHolder {
        private static final AccountService instance = new AccountService();
    }

    public static AccountService getInstance() {

        return AccountService.InstanceHolder.instance;
    }

    private AccountService() {}

    private final Database database = Database.getInstance();

    public Account createAccount(User user, Password password) {
        encryptPassword(password);

        user.setUuid(UUID.randomUUID());

        Account account = new Account();
        account.setUser(user);
        account.setPassword(password);

        account = database.saveAccount(account);

        return account;
    }

    public Account assignHmac(Account account) {
        Key newKey = CryptographyService.generateKey("HmacSHA256");
        account.setHmacKey(newKey);
        return database.saveAccount(account);
    }

    public Account findAccount(String number) {
        return database.findAccount(UUID.fromString(number)).orElseThrow();
    }

    public BigDecimal getBalance(String number) {
        Account account = findAccount(number);

        return account.getBalance();
    }

    public BigDecimal addBalance(String number, BigDecimal amount) {
        Account account = findAccount(number);
        account.setBalance(account.getBalance().add(amount));
        database.saveAccount(account);

        return account.getBalance();
    }

    public BigDecimal subtractBalance(String number, BigDecimal amount) {
        Account account = findAccount(number);

        if (account.getBalance().compareTo(amount) < 0)
            throw new IllegalArgumentException("Saldo insuficiente");

        account.setBalance(account.getBalance().subtract(amount));
        database.saveAccount(account);

        return account.getBalance();
    }

    public BigDecimal transferBalance(String sourceNumber, String destinationNumber, BigDecimal amount) {
        Account source = findAccount(sourceNumber);

        if (source.getBalance().compareTo(amount) < 0)
            throw new IllegalArgumentException("Saldo insuficiente");

        Account destination = findAccount(destinationNumber);

        source.setBalance(source.getBalance().subtract(amount));
        destination.setBalance(destination.getBalance().add(amount));

        database.saveAccount(source);
        database.saveAccount(destination);

        return source.getBalance();
    }

    public void encryptPassword(Password password) {
        password.setValue(CryptographyService.encryptPlayfair(password.getValue()));
        password.setEncrypted(true);
    }
}
