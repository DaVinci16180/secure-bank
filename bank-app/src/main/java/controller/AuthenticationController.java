package src.main.java.controller;

import src.main.java.model.Account;
import src.main.java.model.Password;
import src.main.java.model.User;
import src.main.java.network.Request;
import src.main.java.network.Response;
import src.main.java.network.annotations.Controller;
import src.main.java.network.annotations.Path;
import src.main.java.security.CryptographyService;
import src.main.java.service.AccountService;
import src.main.java.service.UserService;

import java.util.UUID;

@Controller(path = "auth")
public class AuthenticationController {

    private static final class InstanceHolder {
        private static final AuthenticationController instance = new AuthenticationController();
    }

    public static AuthenticationController getInstance() {
        return AuthenticationController.InstanceHolder.instance;
    }

    private final UserService userService = UserService.getInstance();
    private final AccountService accountService = AccountService.getInstance();

    @Path(value = "login", _public = true )
    public Object login(Request request) {
        String number = (String) request.getBody().get("account-number");
        String password = (String) request.getBody().get("password");
        Response response = new Response();

        try {
            UUID sessionId = userService.authenticate(number, new Password(password));
            response.addBody("session", sessionId);
        } catch (Exception e) {
            response.addBody("success", false);
            response.addBody("message", "Usuário ou senha incorretos.");
            return response;
        }

        Account account = accountService.findAccount(number);

        response.addBody("success", true);
        response.addBody("user-name", account.getUser().getName());
        response.addBody("account-number", account.getNumber().toString());

        return response;
    }

    @Path(value = "register", _public = true )
    public Object register(Request request) {
        String name = (String) request.getBody().get("name");
        String cpf = (String) request.getBody().get("cpf");
        String phone = (String) request.getBody().get("phone");
        String address = (String) request.getBody().get("address");
        String pass = (String) request.getBody().get("password");

        User user = new User();
        user.setName(name);
        user.setCpf(cpf);
        user.setPhone(phone);
        user.setAddress(address);

        Password password = new Password(pass);

        Account account = accountService.createAccount(user, password);
        account = accountService.assignHmac(account);
        UUID sessionId = userService.authenticate(account, pass);

        Response response = new Response();
        response.addBody("success", true);
        response.addBody("hmac", account.getHmacKey());
        response.addBody("session", sessionId);
        response.addBody("user-name", account.getUser().getName());
        response.addBody("account-number", account.getNumber());

        System.out.println("Nova conta adicionada: " + account.getNumber());

        return response;
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
    @Path(value = "configureHmac", _public = true)
    public Object configureHmac(Request request) {
        String number = (String) request.getBody().get("account-number");
        String password = (String) request.getBody().get("password");
        userService.authenticate(number, new Password(password));

        Account account = accountService.findAccount(number);
        Response response = new Response();

        if (account.hasHmacKey()) {
            response.addBody("success", false);
            response.addBody("message", "Já existe uma chave de acesso configurada para a conta.");
            return response;
        }

        account = accountService.assignHmac(account);
        response.addBody("success", true);
        response.addBody("hmac", account.getHmacKey());

        return response;
    }

    @Path(value = "logout" )
    public Object logout(Request request) {
        UUID sessionId = (UUID) request.getHeaders().get("session");
        userService.endSession(sessionId);

        Response response = new Response();
        response.addBody("success", true);

        return response;
    }

}
