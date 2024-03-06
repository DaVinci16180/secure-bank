package main.java.actions;

import main.java.network.Client;
import main.java.network.LocalStorage;
import src.main.java.Request;
import src.main.java.Response;

import java.security.Key;
import java.util.UUID;

public class UserActions {
    private static final LocalStorage storage = LocalStorage.getInstance();
    private static final Client client = Client.getInstance();

    public static void login(String accountNumber, String password) {
        Request request = new Request();
        request.setPath("auth/login");

        request.addBody("account-number", accountNumber);
        request.addBody("password", password);

        Response response = client.execute(request, true);
        accountNumber = (String) response.getBody().get("account-number");
        storage.sessionId = (UUID) response.getBody().get("session");
        storage.userName = (String) response.getBody().get("user-name");
        storage.accountNumber = accountNumber;
    }

    public static void register(
            String name,
            String cpf,
            String phone,
            String address,
            String password
    ) {
        Request request = new Request();
        request.setPath("auth/register");

        request.addBody("name", name);
        request.addBody("cpf", cpf);
        request.addBody("phone", phone);
        request.addBody("address", address);
        request.addBody("password", password);

        Response response = client.execute(request, false);
        UUID accountNumber = (UUID) response.getBody().get("account-number");
        storage.hmac = (Key) response.getBody().get("hmac");
        storage.sessionId = (UUID) response.getBody().get("session");
        storage.userName = (String) response.getBody().get("user-name");
        storage.accountNumber = accountNumber.toString();
    }

    /**
     * Da forma que está implementado, o usuário só tem
     * acesso à chave Hmac no ato do cadastro de uma nova
     * conta. Além disso, o login exige assinatura. Devido
     * a isso, as contas criadas no setup inicial não
     * possuirão uma chave e não poderão ser acessadas.
     * Este método, usado apenas para depuração, cria e
     * retorna uma chave Hmac para essas contas.
     */
    public static void configureHmac(String accountNumber, String password) {
        Request request = new Request();
        request.setPath("auth/configureHmac");

        request.addBody("account-number", accountNumber);
        request.addBody("password", password);

        Response response = client.execute(request, false);
        storage.hmac = (Key) response.getBody().get("hmac");
    }

    public static void logout() {
        Request request = new Request();
        request.setPath("auth/logout");

        request.addBody("account-number", storage.accountNumber);

        client.execute(request, true);
        storage.clearSession();
    }
}
