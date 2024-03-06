import main.java.network.Client;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import src.main.java.Request;
import src.main.java.Response;

import java.math.BigDecimal;

class AttackTest {

    private final Client client = Client.getInstance();

    String accountNumber = "1084b9cd-a77e-4187-bf4f-04888bfa00a2";
    String password = "senha123";

    @Test
    void login() {
        Request request = new Request();
        request.setPath("auth/login");

        request.addBody("account-number", accountNumber);
        request.addBody("password", password);

        Response response = client.execute(request, false);

        boolean success = (Boolean) response.getBody().get("success");
        String message = (String) response.getBody().get("message");

        Assertions.assertFalse(success);
        Assertions.assertEquals(message, "Acesso negado!");
    }

    @Test
    void getBalance() {
        Request request = new Request();
        request.setPath("account/balance");

        request.addBody("account-number", accountNumber);
        Response response = client.execute(request, true);

        boolean success = (Boolean) response.getBody().get("success");
        String message = (String) response.getBody().get("message");

        Assertions.assertFalse(success);
        Assertions.assertEquals(message, "Acesso negado!");
    }

    @Test
    void deposit() {
        Request request = new Request();
        request.setPath("account/deposit");

        request.addBody("account-number", accountNumber);
        request.addBody("ammount", new BigDecimal("0"));
        Response response = client.execute(request, true);

        boolean success = (Boolean) response.getBody().get("success");
        String message = (String) response.getBody().get("message");

        Assertions.assertFalse(success);
        Assertions.assertEquals(message, "Acesso negado!");
    }

    @Test
    void withdraw() {
        Request request = new Request();
        request.setPath("account/withdraw");

        request.addBody("account-number", accountNumber);
        request.addBody("ammount", new BigDecimal("0"));
        Response response = client.execute(request, true);

        boolean success = (Boolean) response.getBody().get("success");
        String message = (String) response.getBody().get("message");

        Assertions.assertFalse(success);
        Assertions.assertEquals(message, "Acesso negado!");
    }

    @Test
    void transfer() {
        Request request = new Request();
        request.setPath("account/transfer");

        request.addBody("account-number", accountNumber);
        request.addBody("destination-account-number", "b5c93571-03a8-4268-a488-6ad7b81b186b");
        request.addBody("ammount", new BigDecimal("0"));
        Response response = client.execute(request, true);

        boolean success = (Boolean) response.getBody().get("success");
        String message = (String) response.getBody().get("message");

        Assertions.assertFalse(success);
        Assertions.assertEquals(message, "Acesso negado!");
    }
}