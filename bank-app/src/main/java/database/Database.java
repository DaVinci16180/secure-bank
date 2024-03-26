package src.main.java.database;

import src.main.java.security.CryptographyService;
import src.main.java.model.Account;

import java.util.*;

public class Database {


    private static final class InstanceHolder {
        private static final Database instance = new Database();
    }

    public static Database getInstance() {

        return InstanceHolder.instance;
    }

    private final HashMap<UUID, Account> accounts = new HashMap<>();
    private final Map<UUID, UUID> sessions = new HashMap<>();

    public Optional<UUID> findSession(UUID sessionId) {
        return Optional.ofNullable(sessions.get(sessionId));
    }

    public Optional<Account> findAccount(UUID number) {
        return Optional.ofNullable(accounts.get(number));
    }

    public Account saveAccount(Account account) {
        if (account.getNumber() != null && accounts.containsKey(account.getNumber())) {
            accounts.replace(account.getNumber(), account);
            return account;
        }

        account.setNumber(UUID.randomUUID());
        accounts.put(account.getNumber(), account);

        System.out.println();
        System.out.println("Accounts:");
        for (var entry : accounts.entrySet())
            System.out.println(" -> " + entry.getValue().getNumber());
        System.out.println();

        return account;
    }

    public UUID saveSession(Account account) {
        UUID sessionId = UUID.randomUUID();
        sessions.put(sessionId, account.getNumber());

        System.out.println();
        System.out.println("Sessions:");
        for (var entry : sessions.entrySet())
            System.out.println("-> " + entry.getKey() + ": " + entry.getValue());
        System.out.println();

        return sessionId;
    }

    public void deleteSession(UUID sessionId) {
        sessions.remove(sessionId);

        System.out.println();
        System.out.println("Sessions:");
        for (var entry : sessions.entrySet())
            System.out.println(entry.getKey() + ": " + entry.getValue());
        System.out.println();
    }
}
