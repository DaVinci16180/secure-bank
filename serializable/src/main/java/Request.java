package src.main.java;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Request implements Serializable {
    private String path;
    private final Map<String, Object> headers = new HashMap<>();
    private final Map<String, Object> body = new HashMap<>();

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public Request addHeader(String key, Object value) {
        headers.put(key, value);
        return this;
    }

    public Request removeHeader(String key) {
        headers.remove(key);
        return this;
    }

    public Request addBody(String key, Object value) {
        body.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Request{" + "path='" + path + '\'' + ", headers={");

        for (var entry : headers.entrySet()) {
            if (entry.getKey().equals("rsa-public-key")) {
                sb
                        .append(entry.getKey())
                        .append("=")
                        .append("Sun RSA public key, 2048 bits");
            } else {
                sb
                        .append(entry.getKey())
                        .append("=")
                        .append(entry.getValue());
            }
        }

        sb.append(", body=").append(body).append('}');

        return sb.toString();
    }
}
