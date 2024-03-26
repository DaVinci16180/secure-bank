package src.main.java.security;

import java.io.Serializable;
import java.math.BigInteger;

public record CustomKey (
    BigInteger value,
    BigInteger mod
) implements Serializable {}
