package main.java;

import main.java.menus.Menus;
import main.java.network.LocalStorage;

public class App {

    private static final LocalStorage storage = LocalStorage.getInstance();

    public static void main(String[] args) {
        while (true) {
            try {
                Menus.main();
            } catch (SecurityException e) {
                storage.reset();
                storage.error = true;
                storage.errorMessage = e.getMessage();
            } catch (Exception e) {
                storage.reset();
                storage.error = true;
                storage.errorMessage = "Erro inesperado. Faça login novamente.";
            }
        }
    }
}
