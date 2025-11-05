package portal.util;

import java.util.Scanner;

public class InputUtil {
    private static final Scanner sc = new Scanner(System.in);

    public static String readNonEmpty(String prompt) {
        String input;
        do {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (input.isEmpty()) System.out.println("Input cannot be empty. Try again.");
        } while (input.isEmpty());
        return input;
    }

    public static int readInt(String prompt) {
         
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number!");
            }
        }
    }
}
