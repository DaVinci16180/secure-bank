package main.java.menus;

import main.java.actions.AccountActions;
import main.java.actions.InvestmentActions;
import main.java.actions.UserActions;
import main.java.network.LocalStorage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Scanner;

public class Menus {

    private static final Scanner scanner = new Scanner(System.in);
    private static final LocalStorage storage = LocalStorage.getInstance();

    public static void main() {
        while (true) {
            int opt = -1;

            while (opt < 1 || opt > 2) {
                clear();
                System.out.println();
                System.out.println("1 - Login");
                System.out.println("2 - Cadastro");
                System.out.println();
                System.out.print("-> ");

                try {
                    opt = Integer.parseInt(scanner.nextLine());
                } catch (Exception ignored) {}
            }

            if (opt == 1)
                login();
            else
                register();
        }
    }

    private static void login() {
        clear();
        System.out.println();
        System.out.print("# Número da conta -> ");
        String accountNumber = scanner.nextLine();
        System.out.print("# Senha -> ");
        String password = scanner.nextLine();

        // Apenas para depuração
        if (storage.hmac == null)
            UserActions.configureHmac(accountNumber, password);

        UserActions.login(accountNumber, password);

        home();
    }

    private static void register() {
        clear();
        System.out.println();
        System.out.print("# Nome -> ");
        String name = scanner.nextLine();
        System.out.print("# CPF -> ");
        String cpf = scanner.nextLine();
        System.out.print("# Telefone -> ");
        String phone = scanner.nextLine();
        System.out.print("# Endereço -> ");
        String address = scanner.nextLine();
        System.out.println();
        System.out.print("# Senha -> ");
        String password = scanner.nextLine();

        UserActions.register(name, cpf, phone, address, password);

        home();
    }

    private static void home() {
        while (storage.sessionId != null) {
            int opt = -1;

            while (opt < 0 || opt > 4) {
                clear();
                System.out.println();
                System.out.println("1 - Depósito");
                System.out.println("2 - Saque");
                System.out.println("3 - Transferência");
                System.out.println("4 - Investimentos");
                System.out.println();
                System.out.println("0 - Sair");
                System.out.println();
                System.out.print("-> ");

                try {
                    opt = Integer.parseInt(scanner.nextLine());
                } catch (Exception ignored) {}
            }

            switch (opt) {
                case 0 -> UserActions.logout();
                case 1 -> deposit();
                case 2 -> withdraw();
                case 3 -> transfer();
                case 4 -> investments();
            }
        }
    }

    private static void deposit() {
        clear();
        System.out.println();
        System.out.print("# Valor -> ");
        BigDecimal amount = readNumber();
        AccountActions.deposit(amount);
    }

    private static void withdraw() {
        clear();
        System.out.println();
        System.out.print("# Valor -> ");
        BigDecimal amount = readNumber();
        AccountActions.withdraw(amount);
    }

    private static void transfer() {
        clear();
        System.out.println();
        System.out.print("# Valor -> ");
        BigDecimal amount = readNumber();
        System.out.print("# Número da conta de destino -> ");
        String destination = scanner.nextLine();
        AccountActions.transfer(amount, destination);
    }

    private static void investments() {
        boolean keepGoing = true;
        while (keepGoing) {
            int opt = -1;

            while (opt < 0 || opt > 2) {
                clear();
                System.out.println();
                System.out.println("1 - Poupança");
                System.out.println("2 - Renda fixa");
                System.out.println();
                System.out.println("0 - Voltar");
                System.out.println();
                System.out.print("-> ");

                try {
                    opt = Integer.parseInt(scanner.nextLine());
                } catch (Exception ignored) {}
            }

            switch (opt) {
                case 0 -> keepGoing = false;
                case 1 -> savings();
                case 2 -> fixedIncome();
            }
        }
    }

    private static void savings() {
        Map<Integer, BigDecimal> prognosis = InvestmentActions.savingsPrognosis();
        System.out.println();

        clear();
        prognosis(prognosis);
        System.out.println();
        System.out.print("<- Voltar");
        scanner.nextLine();
    }

    private static void fixedIncome() {
        boolean keepGoing = true;
        while (keepGoing) {
            Map<Integer, BigDecimal> prognosis = InvestmentActions.fixedIncomePrognosis();
            BigDecimal balance = InvestmentActions.fixedIncomeBalance();
            int opt = -1;

            while (opt < 0 || opt > 1) {
                clear(balance);
                prognosis(prognosis);
                System.out.println();
                System.out.println("1 - Investir");
                System.out.println();
                System.out.println("0 - Voltar");
                System.out.println();
                System.out.print("-> ");

                try {
                    opt = Integer.parseInt(scanner.nextLine());
                } catch (Exception ignored) {}
            }

            switch (opt) {
                case 0 -> keepGoing = false;
                case 1 -> invest();
            }
        }
    }

    private static void prognosis(Map<Integer, BigDecimal> prognosis) {
        System.out.println();

        int padding = 10;
        padding(padding);
        repeat('-', 26, true);
        padding(padding);
        System.out.print("|");
        div("Prognóstico", 24, Alignment.CENTER);
        System.out.println("|");
        padding(padding);
        repeat('=', 26, true);

        padding(padding);
        System.out.print("|");
        div("Mês", 6, Alignment.CENTER);
        System.out.print("|");
        div("Saldo", 17, Alignment.CENTER);
        System.out.println("|");
        padding(padding);
        repeat('=', 26, true);

        for (var entry : prognosis.entrySet()) {
            padding(padding);
            System.out.print("|");
            div(String.format("%02d", entry.getKey()), 6, Alignment.CENTER);
            System.out.print("|");
            div("R$ " + entry.getValue().setScale(2, RoundingMode.HALF_DOWN), 17, Alignment.RIGHT);
            System.out.println("|");
        }

        padding(padding);
        repeat('-', 26, true);
    }

    private static void invest() {
        clear();
        System.out.println();
        System.out.print("# Valor -> ");
        BigDecimal amount = readNumber();
        InvestmentActions.invest(amount);
    }

    private static void sign() {
        System.out.print("""
                 ______     _____    __    __   _____     _____
                /       \\  /      \\ /  \\  /  | /      \\  /      \\\s
                ¡¡¡¡¡¡¡  |/¡¡¡¡¡¡  |¡¡  \\ ¡¡ |/¡¡¡¡¡¡  |/¡¡¡¡¡¡  |
                ¡¡ |__¡¡ |¡¡ |__¡¡ |¡¡¡  \\¡¡ |¡¡ |  ¡¡/ ¡¡ |  ¡¡ |
                ¡¡    ¡¡< ¡¡    ¡¡ |¡¡¡¡  ¡¡ |¡¡ |      ¡¡ |  ¡¡ |
                ¡¡¡¡¡¡¡  |¡¡¡¡¡¡¡¡ |¡¡ ¡¡ ¡¡ |¡¡ |   __ ¡¡ |  ¡¡ |
                ¡¡ |__¡¡ |¡¡ |  ¡¡ |¡¡ |¡¡¡¡ |¡¡ \\__/  |¡¡ \\__¡¡ |
                ¡¡    ¡¡/ ¡¡ |  ¡¡ |¡¡ | ¡¡¡ |¡¡    ¡¡/ ¡¡    ¡¡/\s
                ¡¡¡¡¡¡¡/  ¡¡/   ¡¡/ ¡¡/   ¡¡/  ¡¡¡¡¡¡/   ¡¡¡¡¡¡/ \s
                """);
    }

    private static void clear() {
        System.out.println("grep=cls");
        error();
        sign();

        if (storage.sessionId != null) {
            div(storage.accountNumber, 48, Alignment.RIGHT);
            System.out.println();
            div("Bem-Vindo(a) " + storage.userName, 48, Alignment.RIGHT);
            System.out.println();
            div("Saldo: R$ " + AccountActions.getBalance().setScale(2, RoundingMode.HALF_DOWN), 48, Alignment.CENTER);
            System.out.println();
        }
    }

    private static void clear(BigDecimal balance) {
        System.out.println("grep=cls");
        error();
        sign();

        if (storage.sessionId != null) {
            div(storage.accountNumber, 48, Alignment.RIGHT);
            System.out.println();
            div("Bem-Vindo(a) " + storage.userName, 48, Alignment.RIGHT);
            System.out.println();
            div("Saldo (renda fixa): R$ " + balance.setScale(2, RoundingMode.HALF_DOWN), 48, Alignment.CENTER);
            System.out.println();
        }
    }

    private static void error() {
        if (storage.error) {
            div("Erro - " + storage.errorMessage, 50, Alignment.CENTER);
            storage.resetError();
        }

        System.out.println();
    }

    public enum Alignment { LEFT, CENTER, RIGHT }

    public static void div(String text, int size, Alignment alignment) {
        if (text.length() >= size - 2) {
            System.out.print(" " + text.substring(0, size - 2) + " ");
            return;
        }

        int spaces = size - text.length() - 2;

        if (spaces == 1)
            alignment = Alignment.LEFT;

        switch (alignment) {
            case LEFT -> {
                System.out.print(" " + text);
                System.out.format(String.format("%1$" + spaces + "s ", ""));
            }
            case CENTER -> {
                System.out.format(String.format(" %1$" + Math.floor(spaces / 2.) + "s", ""));
                System.out.print(text);
                System.out.format(String.format("%1$" + Math.ceil(spaces / 2.) + "s ", ""));
            }
            case RIGHT -> {
                System.out.format(String.format(" %1$" + spaces + "s", ""));
                System.out.print(text + " ");
            }
        }
    }

    public static void repeat(char ch, int times, boolean br) {
        System.out.print(String.format("%1$" + times + "s", "").replace(' ', ch));
        if (br)
            System.out.println();
    }

    public static void padding(int size) {
        System.out.format("%1$" + size + "s", "");
    }

    private static BigDecimal readNumber() {
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (Exception ignored) {
                System.out.print("Erro - insira um número válido -> ");
            }
        }
    }
}
