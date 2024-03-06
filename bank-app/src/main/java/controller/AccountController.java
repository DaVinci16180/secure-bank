package src.main.java.controller;

import src.main.java.Request;
import src.main.java.Response;
import src.main.java.network.annotations.Controller;
import src.main.java.network.annotations.Path;
import src.main.java.service.AccountService;

import java.math.BigDecimal;

@Controller(path = "account")
public class AccountController {

    private static final class InstanceHolder {
        private static final AccountController instance = new AccountController();
    }

    public static AccountController getInstance() {

        return AccountController.InstanceHolder.instance;
    }

    private AccountController() {}

    private final AccountService accountService = AccountService.getInstance();

    @Path(value = "balance")
    public Object getBalance(Request request) {
        String number = (String) request.getBody().get("account-number");
        BigDecimal balance = accountService.getBalance(number);

        Response response = new Response();
        response.addBody("success", true);
        response.addBody("balance", balance);

        return response;
    }

    @Path(value = "deposit")
    public Object deposit(Request request) {
        String number = (String) request.getBody().get("account-number");
        BigDecimal amount = (BigDecimal) request.getBody().get("amount");

        BigDecimal newBalance = accountService.addBalance(number, amount);

        Response response = new Response();
        response.addBody("success", true);
        response.addBody("balance", newBalance);

        return response;
    }

    @Path(value = "withdraw")
    public Object withdraw(Request request) {
        String number = (String) request.getBody().get("account-number");
        BigDecimal amount = (BigDecimal) request.getBody().get("amount");

        BigDecimal newBalance = accountService.subtractBalance(number, amount);

        Response response = new Response();
        response.addBody("success", true);
        response.addBody("balance", newBalance);

        return response;
    }

    @Path(value = "transfer")
    public Object transfer(Request request) {
        String source = (String) request.getBody().get("account-number");
        String destination = (String) request.getBody().get("destination-account-number");
        BigDecimal amount = (BigDecimal) request.getBody().get("amount");

        BigDecimal newBalance = accountService.transferBalance(source, destination, amount);

        Response response = new Response();
        response.addBody("success", true);
        response.addBody("balance", newBalance);

        return response;
    }
}
