package src.main.java.network;

import java.io.Serializable;

public record Package (
    byte[] key,
    byte[] data
) implements Serializable {

}
