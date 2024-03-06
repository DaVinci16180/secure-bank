package src.main.java.model;

import java.math.BigDecimal;
import java.security.Key;
import java.util.UUID;

public class Account {
    private UUID number;
    private Password password;
    private Key hmacKey;
    private User user;
    private BigDecimal balance = BigDecimal.ZERO;
    private BigDecimal fixedIncome = BigDecimal.ZERO;

    public UUID getNumber() {
        return number;
    }

    public void setNumber(UUID number) {
        this.number = number;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public BigDecimal getFixedIncome() {
        return fixedIncome;
    }

    public void setFixedIncome(BigDecimal fixedIncome) {
        this.fixedIncome = fixedIncome;
    }

    public boolean hasHmacKey() {
        return hmacKey != null;
    }

    public void setHmacKey(Key hmacKey) {
        this.hmacKey = hmacKey;
    }

    public Key getHmacKey() {
        return this.hmacKey;
    }
}
