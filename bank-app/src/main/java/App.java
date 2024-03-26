package src.main.java;

import src.main.java.model.Password;
import src.main.java.model.User;
import src.main.java.network.RSAServer;
import src.main.java.network.Server;
import src.main.java.security.CryptographyService;
import src.main.java.service.AccountService;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;

public class App {

    private static final AccountService accountService = AccountService.getInstance();

    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        setup();

        try {
            new Thread(new Server(8080)).start();
            new Thread(new RSAServer(8081)).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void setup() {
        User user = new User();
        user.setAddress("Rua Tal, 123");
        user.setName("Moacir Demóstenes");
        user.setCpf("111.111.111-11");
        user.setPhone("9 91111-1111");

        Password password = new Password("senha123");

        accountService.createAccount(user, password);

        user = new User();
        user.setAddress("Rua Tal, 123");
        user.setName("Joaquim Santiago");
        user.setCpf("222.222.222-22");
        user.setPhone("9 92222-2222");

        password = new Password("senha123");

        accountService.createAccount(user, password);

        user = new User();
        user.setAddress("Rua Tal, 123");
        user.setName("Fátima Damires");
        user.setCpf("333.333.333-33");
        user.setPhone("9 93333-3333");

        password = new Password("senha123");

        accountService.createAccount(user, password);
    }
}
