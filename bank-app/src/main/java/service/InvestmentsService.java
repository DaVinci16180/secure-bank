package src.main.java.service;

import src.main.java.database.Database;
import src.main.java.model.Account;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class InvestmentsService {

    private static final class InstanceHolder {
        private static final InvestmentsService instance = new InvestmentsService();
    }

    public static InvestmentsService getInstance() {
        return InvestmentsService.InstanceHolder.instance;
    }

    private InvestmentsService() {}

    private final Database database = Database.getInstance();
    private final AccountService accountService = AccountService.getInstance();

    public BigDecimal transferBalanceToFixedIncome(String number, BigDecimal amount) {
        Account account = accountService.findAccount(number);

        if (account.getBalance().compareTo(amount) < 0)
            throw new IllegalArgumentException("Saldo insuficiente");

        account.setBalance(account.getBalance().subtract(amount));
        account.setFixedIncome(account.getFixedIncome().add(amount));

        database.saveAccount(account);

        return account.getBalance();
    }
    public Map<Integer, BigDecimal> getSavingsPrognosis(String number) {
        Map<Integer, BigDecimal> prognosis = new HashMap<>();
        Account account = accountService.findAccount(number);

        BigDecimal performance = new BigDecimal("0.005");
        BigDecimal yield = account.getBalance().multiply(performance);
        prognosis.put(1, account.getBalance().add(yield));

        for (int i = 2; i <= 12; i++) {
            yield = prognosis.get(i - 1).multiply(performance);
            prognosis.put(i, prognosis.get(i - 1).add(yield));
        }

        return prognosis;
    }

    public Map<Integer, BigDecimal> getFixedIncomePrognosis(String number) {
        Map<Integer, BigDecimal> prognosis = new HashMap<>();
        Account account = accountService.findAccount(number);

        BigDecimal performance = new BigDecimal("0.015");
        BigDecimal yield = account.getFixedIncome().multiply(performance);
        prognosis.put(1, account.getFixedIncome().add(yield));

        for (int i = 2; i <= 12; i++) {
            yield = prognosis.get(i - 1).multiply(performance);
            prognosis.put(i, prognosis.get(i - 1).add(yield));
        }

        return prognosis;
    }

    public BigDecimal getFixedIncomeBalance(String number) {
        Account account = accountService.findAccount(number);

        return account.getFixedIncome();
    }
}
