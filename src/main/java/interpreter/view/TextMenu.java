package interpreter.view;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class TextMenu {
    private final Map<String, Command> commands = new LinkedHashMap<>();

    public void addCommand(Command c) {
        commands.put(c.getKey(), c);
    }

    public void show() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n===== Toy Language Interpreter =====");
            commands.values().forEach(System.out::println);
            System.out.print("Select option: ");
            String choice = scanner.nextLine().trim();
            Command cmd = commands.get(choice);
            if (cmd == null) {
                System.out.println("Invalid option: " + choice);
            } else {
                cmd.execute();
            }
        }
    }
}

