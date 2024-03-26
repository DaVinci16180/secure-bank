package src.main.java.controller;

import src.main.java.network.Request;
import src.main.java.network.Response;
import src.main.java.network.annotations.Controller;
import src.main.java.network.annotations.Path;
import src.main.java.service.InvestmentsService;

import java.math.BigDecimal;
import java.util.Map;

@Controller(path = "investments")
public class InvestmentsController {

    private static final class InstanceHolder {
        private static final InvestmentsController instance = new InvestmentsController();
    }

    public static InvestmentsController getInstance() {

        return InvestmentsController.InstanceHolder.instance;
    }

    private InvestmentsController() {}

    private final InvestmentsService investmentsService = InvestmentsService.getInstance();

    @Path(value = "savingsPrognosis")
    public Object savingsPrognosis(Request request) {
        String number = (String) request.getBody().get("account-number");

        Map<Integer, BigDecimal> prognosis = investmentsService.getSavingsPrognosis(number);

        Response response = new Response();
        response.addBody("success", true);
        response.addBody("prognosis", prognosis);

        return response;
    }

    @Path(value = "fixedIncomePrognosis")
    public Object fixedIncomePrognosis(Request request) {
        String number = (String) request.getBody().get("account-number");

        Map<Integer, BigDecimal> prognosis = investmentsService.getFixedIncomePrognosis(number);

        Response response = new Response();
        response.addBody("success", true);
        response.addBody("prognosis", prognosis);

        return response;
    }

    @Path(value = "fixedIncomeBalance")
    public Object fixedIncomeBalance(Request request) {
        String number = (String) request.getBody().get("account-number");

        BigDecimal balance = investmentsService.getFixedIncomeBalance(number);

        Response response = new Response();
        response.addBody("success", true);
        response.addBody("balance", balance);

        return response;
    }

    @Path(value = "invest")
    public Object invest(Request request) {
        String number = (String) request.getBody().get("account-number");
        BigDecimal amount = (BigDecimal) request.getBody().get("amount");

        BigDecimal newBalance = investmentsService.transferBalanceToFixedIncome(number, amount);

        Response response = new Response();
        response.addBody("success", true);
        response.addBody("balance", newBalance);

        return response;
    }
}
