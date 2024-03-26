package src.main.java.network;

import src.main.java.security.CryptographyService;
import src.main.java.security.CustomKey;
import src.main.java.security.CustomKeyPair;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RSAServer implements Runnable {
    private static final CustomKeyPair keyPair = CryptographyService.generateKeyPair();
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
                        out.writeObject(keyPair.publicKey());

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

    public static CustomKey getPrivateKey() {
        return keyPair.privateKey();
    }
}
