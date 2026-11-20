package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;
import interpreter.model.adt.MyDictionary;
import interpreter.model.adt.MyList;
import interpreter.model.adt.MyStack;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;
import interpreter.repository.Repository;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class View {
    private final Scanner scanner;
    private final PrintStream out;
    private boolean displayStepByStep = true;

    public View() {
        this(System.in, System.out);
    }

    public View(InputStream in, PrintStream out) {
        this.scanner = new Scanner(in);
        this.out = out;
    }

    public boolean isDisplayStepByStep() {
        return displayStepByStep;
    }

    public void setDisplayStepByStep(boolean displayStepByStep) {
        this.displayStepByStep = displayStepByStep;
    }

    public void runProgram(IStmt program) {
        try {
            PrgState state = PrgState.of(program);
            IRepository repo = new Repository(state);
            Controller controller = new Controller(repo, displayStepByStep);
            out.println("--- Starting execution ---");
            controller.allStep();
            out.println("--- Execution completed successfully ---");
            out.println("Final Output: " + state.getOut());
        } catch (MyException e) {
            out.println("Execution Error: " + e.getMessage());
        } catch (Exception e) {
            out.println("Unexpected Error: " + e.getMessage());
        }
    }

    public void start() {
        while (true) {
            printMenu();
            out.print("Choose an option: ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String line = scanner.nextLine().trim();
            switch (line) {
                case "1" -> runProgram(Examples.EX1);
                case "2" -> runProgram(Examples.EX2);
                case "3" -> runProgram(Examples.EX3);
                case "4" -> {
                    displayStepByStep = !displayStepByStep;
                    out.println("Step-by-step display is now " + (displayStepByStep ? "ON" : "OFF"));
                }
                case "0" -> {
                    out.println("Goodbye!");
                    return;
                }
                default -> out.println("Invalid option. Please try again.");
            }
            out.println();
        }
    }

    private void printMenu() {
        out.println("Toy Language Interpreter");
        out.println("1. Run Example 1: int v; v = 2; print(v)");
        out.println("2. Run Example 2: int a; int b; a = 2 + 3 * 5; b = a + 1; print(b)");
        out.println("3. Run Example 3: bool a; int v; a = true; (if a then v = 2 else v = 3); print(v)");
        out.println("4. Toggle step-by-step display (currently " + (displayStepByStep ? "ON" : "OFF") + ")");
        out.println("0. Exit");
    }
}

