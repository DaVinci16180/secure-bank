package src.main.java.model;

import java.security.Key;
import java.util.UUID;

public class User {
    private UUID uuid;
    private String cpf;
    private String name;
    private String phone;
    private String address;
    private Key hmac;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Key getHmac() {
        return hmac;
    }

    public void setHmac(Key hmac) {
        this.hmac = hmac;
    }
}
