package src.main.java.network;

import src.main.java.CryptographyService;
import src.main.java.Package;
import src.main.java.Request;
import src.main.java.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.security.PublicKey;

public class Server implements Runnable{

    private final ServerSocket serverSocket;
    private final RequestHandler handler = RequestHandler.getInstance();
    private boolean running = true;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            while (running) {
                Socket requester = serverSocket.accept();

                Thread thread = new Thread(() -> {
                    ObjectInputStream in = null;
                    ObjectOutputStream out = null;
                    try {
                        in = new ObjectInputStream(requester.getInputStream());
                        out = new ObjectOutputStream(requester.getOutputStream());

                        Request request = getRequest(in);
                        Response response = handler.handle(request);

                        PublicKey userKey = (PublicKey) request.getHeaders().get("rsa-public-key");
                        Package pack = generatePackage(response, userKey);

                        out.writeObject(pack);
                        out.flush();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } finally {
                        try {
                            in.close();
                            out.close();
                            requester.close();
                        } catch (IOException ignored) { }
                    }
                });

                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Servidor encerrado.");
        }
    }

    public Package generatePackage(Response response, PublicKey userKey) {
        Key simetricKey = CryptographyService.generateKey("AES");
        byte[] data = CryptographyService.encryptAES(response, simetricKey);
        byte[] encryptedKey = CryptographyService.encryptRSA(simetricKey, userKey);
        return new Package(encryptedKey, data);
    }

    private Request getRequest(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Package pack = (Package) in.readObject();
        Key simetricKey = (Key) CryptographyService.decryptRSA(pack.key(), RSAServer.getPrivateKey());
        return (Request) CryptographyService.decryptAES(pack.data(), simetricKey);
    }

    public void shutDown() throws IOException {
        serverSocket.close();
        running = false;
    }
}