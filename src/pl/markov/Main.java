package pl.markov;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Environment e = new Environment();
        e.readFile("src/map3.txt");
        e.showFile("src/map3.txt");
        System.out.println();
        System.out.println();
        e.showEnvironment();
        System.out.println();
        System.out.println();
        e.showUtility();
        System.out.println();
        e.calculateUtility();
        e.showUtility();
        System.out.println();
        e.showOptimalActions();
    }

}
