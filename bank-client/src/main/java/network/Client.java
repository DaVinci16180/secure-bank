package main.java.network;

import src.main.java.CryptographyService;
import src.main.java.Package;
import src.main.java.Request;
import src.main.java.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.Key;
import java.security.PublicKey;

public class Client {

    private static final class InstanceHolder {
        private static final Client instance = new Client();
    }

    public static Client getInstance() {

        return Client.InstanceHolder.instance;
    }

    private final LocalStorage storage = LocalStorage.getInstance();

    private Client() {}

    public Response execute(Request request, boolean sign) {
        verifyServerKey();
        try(Socket socket = new Socket("0.0.0.0", 8080)) {
            if (sign && storage.hmac == null) {
                storage.error = true;
                throw new SecurityException("Chave de acesso (Hmac) não configurada.");
            }

            request.addHeader("rsa-public-key", storage.keyPair.getPublic());
            request.addHeader("session", storage.sessionId);

            if (sign)
                request.addHeader("sign", CryptographyService.sign(request, storage.hmac));

            Key key = CryptographyService.generateKey("AES");

            byte[] encryptedData = CryptographyService.encryptAES(request, key);
            byte[] encryptedKey = CryptographyService.encryptRSA(key, storage.serverPublicKey);

            Package send = new Package(encryptedKey, encryptedData);

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(send);
            out.flush();

            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Package recv = (Package) in.readObject();

            key = (Key) CryptographyService.decryptRSA(recv.key(), storage.keyPair.getPrivate());
            Response response = (Response) CryptographyService.decryptAES(recv.data(), key);

            out.close();
            in.close();

            boolean success = (Boolean) response.getBody().get("success");
            if (!success) {
                storage.error = true;
                storage.errorMessage = (String) response.getBody().get("message");
            }

            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void verifyServerKey() {
        if (storage.serverPublicKey == null) {
            try (Socket socket = new Socket("0.0.0.0", 8081);
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
                storage.serverPublicKey = (PublicKey) inputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
