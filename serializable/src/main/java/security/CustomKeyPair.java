package src.main.java.security;

import java.io.Serializable;
import java.math.BigInteger;

public record CustomKeyPair (
    CustomKey privateKey,
    CustomKey publicKey
) implements Serializable {}
