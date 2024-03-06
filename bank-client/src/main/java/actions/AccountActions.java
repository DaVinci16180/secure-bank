package main.java.actions;

import src.main.java.Request;
import src.main.java.Response;
import main.java.network.Client;
import main.java.network.LocalStorage;

import java.math.BigDecimal;

public class AccountActions {

    private static final LocalStorage storage = LocalStorage.getInstance();
    private static final Client client = Client.getInstance();

    public static BigDecimal getBalance() {
        Request request = new Request();
        request.setPath("account/balance");

        request.addBody("account-number", storage.accountNumber);
        Response response = client.execute(request, true);

        return (BigDecimal) response.getBody().get("balance");
    }

    public static void deposit(BigDecimal amount) {
        Request request = new Request();
        request.setPath("account/deposit");

        request.addBody("account-number", storage.accountNumber);
        request.addBody("amount", amount);

        client.execute(request, true);
    }

    public static void withdraw(BigDecimal amount) {
        Request request = new Request();
        request.setPath("account/withdraw");

        request.addBody("account-number", storage.accountNumber);
        request.addBody("amount", amount);

        client.execute(request, true);
    }

    public static void transfer(BigDecimal amount, String target) {
        Request request = new Request();
        request.setPath("account/transfer");

        request.addBody("account-number", storage.accountNumber);
        request.addBody("destination-account-number", target);
        request.addBody("amount", amount);

        client.execute(request, true);
    }
}
