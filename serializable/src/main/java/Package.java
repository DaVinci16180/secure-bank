package src.main.java;

import java.io.Serializable;

public record Package (
    byte[] key,
    byte[] data
) implements Serializable {

}
