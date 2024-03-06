package src.main.java;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Response implements Serializable {
    Map<String, Object> headers = new HashMap<>();
    Map<String, Object> body = new HashMap<>();

    public Response addHeader(String key, Object value) {
        headers.put(key, value);
        return this;
    }

    public Response addBody(String key, Object value) {
        body.put(key, value);
        return this;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Response{" +
                "headers=" + headers +
                ", body=" + body +
                '}';
    }
}
