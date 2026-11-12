package interpreter;

import interpreter.controller.Controller;
import interpreter.model.PrgState;
import interpreter.repository.Repository;
import interpreter.view.ExitCommand;
import interpreter.view.RunExampleCommand;
import interpreter.view.TextMenu;

import static interpreter.view.Examples.*;

public class Main {
    public static void main(String[] args) {
        // Example 1
        PrgState state1 = PrgState.of(EX1);
        Repository repo1 = new Repository(state1, "log1.txt");
        Controller ctrl1 = new Controller(repo1);

        // Example 2
        PrgState state2 = PrgState.of(EX2);
        Repository repo2 = new Repository(state2, "log2.txt");
        Controller ctrl2 = new Controller(repo2);

        // Example 3
        PrgState state3 = PrgState.of(EX3);
        Repository repo3 = new Repository(state3, "log3.txt");
        Controller ctrl3 = new Controller(repo3);

        // Example 4 — requires "test.in" to exist in the working directory
        PrgState state4 = PrgState.of(EX4);
        Repository repo4 = new Repository(state4, "log4.txt");
        Controller ctrl4 = new Controller(repo4);

        // Example 5 — relational expression with if
        PrgState state5 = PrgState.of(EX5);
        Repository repo5 = new Repository(state5, "log5.txt");
        Controller ctrl5 = new Controller(repo5);

        TextMenu menu = new TextMenu();
        menu.addCommand(new RunExampleCommand("1", "int v; v=2; print(v)", ctrl1));
        menu.addCommand(new RunExampleCommand("2", "int a; int b; a=2+3*5; b=a+1; print(b)", ctrl2));
        menu.addCommand(new RunExampleCommand("3", "bool a; int v; a=true; if a then v=2 else v=3; print(v)", ctrl3));
        menu.addCommand(new RunExampleCommand("4", "string fname; int r; open/read/close file", ctrl4));
        menu.addCommand(new RunExampleCommand("5", "int v; v=4; if (v<8) then print(v) else print(0)", ctrl5));
        menu.addCommand(new ExitCommand("0", "Exit"));

        menu.show();
    }
}
