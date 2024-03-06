package main.java.actions;

import src.main.java.Request;
import src.main.java.Response;
import main.java.network.Client;
import main.java.network.LocalStorage;

import java.math.BigDecimal;
import java.util.Map;

public class InvestmentActions {

    private static final LocalStorage storage = LocalStorage.getInstance();
    private static final Client client = Client.getInstance();

    public static Map<Integer, BigDecimal> savingsPrognosis() {
        Request request = new Request();
        request.setPath("investments/savingsPrognosis");

        request.addBody("account-number", storage.accountNumber);
        Response response = client.execute(request, true);

        return (Map<Integer, BigDecimal>) response.getBody().get("prognosis");
    }

    public static Map<Integer, BigDecimal> fixedIncomePrognosis() {
        Request request = new Request();
        request.setPath("investments/fixedIncomePrognosis");

        request.addBody("account-number", storage.accountNumber);
        Response response = client.execute(request, true);

        return (Map<Integer, BigDecimal>) response.getBody().get("prognosis");
    }

    public static BigDecimal fixedIncomeBalance() {
        Request request = new Request();
        request.setPath("investments/fixedIncomeBalance");

        request.addBody("account-number", storage.accountNumber);
        Response response = client.execute(request, true);

        return (BigDecimal) response.getBody().get("balance");
    }

    public static void invest(BigDecimal amount) {
        Request request = new Request();
        request.setPath("investments/invest");

        request.addBody("account-number", storage.accountNumber);
        request.addBody("amount", amount);

        client.execute(request, true);
    }
}
