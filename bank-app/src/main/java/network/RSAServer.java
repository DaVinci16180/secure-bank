package src.main.java.network;

import src.main.java.CryptographyService;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.PrivateKey;

public class RSAServer implements Runnable {
    private static final KeyPair keyPair = CryptographyService.generateKeyPair();
    private final ServerSocket serverSocket;
    private boolean running = true;

    public RSAServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            while (running) {
                Socket requester = serverSocket.accept();

                new Thread(() -> {
                    try {
                        ObjectOutputStream out = new ObjectOutputStream(requester.getOutputStream());
                        out.writeObject(keyPair.getPublic());

                        requester.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
        } catch (IOException e) {
            System.out.println("Servidor encerrado.");
        }
    }

    public void shutDown() throws IOException {
        serverSocket.close();
        running = false;
    }

    public static PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }
}
