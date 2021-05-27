package multi.project.app;

import multi.project.util.Utils;
import victor.lab.exception.NotANumberException;


public class App {
    public static void main(String[] args) {
        try {
            boolean result = Utils.isAllPositiveNumbers("12", "79");
            System.out.println(result);
        } catch (NotANumberException e) {
            System.out.println(e.getMessage());
        }
    }
}