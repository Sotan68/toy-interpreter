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

        // Example 6 — heap allocation
        PrgState state6 = PrgState.of(EX6);
        Repository repo6 = new Repository(state6, "log6.txt");
        Controller ctrl6 = new Controller(repo6);

        // Example 7 — heap reading
        PrgState state7 = PrgState.of(EX7);
        Repository repo7 = new Repository(state7, "log7.txt");
        Controller ctrl7 = new Controller(repo7);

        // Example 8 — heap writing
        PrgState state8 = PrgState.of(EX8);
        Repository repo8 = new Repository(state8, "log8.txt");
        Controller ctrl8 = new Controller(repo8);

        // Example 9 — garbage collector
        PrgState state9 = PrgState.of(EX9);
        Repository repo9 = new Repository(state9, "log9.txt");
        Controller ctrl9 = new Controller(repo9);

        // Example 10 — while loop
        PrgState state10 = PrgState.of(EX10);
        Repository repo10 = new Repository(state10, "log10.txt");
        Controller ctrl10 = new Controller(repo10);

        TextMenu menu = new TextMenu();
        menu.addCommand(new RunExampleCommand("1", "int v; v=2; print(v)", ctrl1));
        menu.addCommand(new RunExampleCommand("2", "int a; int b; a=2+3*5; b=a+1; print(b)", ctrl2));
        menu.addCommand(new RunExampleCommand("3", "bool a; int v; a=true; if a then v=2 else v=3; print(v)", ctrl3));
        menu.addCommand(new RunExampleCommand("4", "string fname; int r; open/read/close file", ctrl4));
        menu.addCommand(new RunExampleCommand("5", "int v; v=5; if (v<8) then print(v) else print(0)", ctrl5));
        menu.addCommand(new RunExampleCommand("6", "Ref int v; new(v,20); Ref Ref int a; new(a,v); print(v); print(a)", ctrl6));
        menu.addCommand(new RunExampleCommand("7", "Ref int v; new(v,20); Ref Ref int a; new(a,v); print(rH(v)); print(rH(rH(a))+5)", ctrl7));
        menu.addCommand(new RunExampleCommand("8", "Ref int v; new(v,20); print(rH(v)); wH(v,30); print(rH(v)+5)", ctrl8));
        menu.addCommand(new RunExampleCommand("9", "Ref int v; new(v,20); Ref Ref int a; new(a,v); new(v,30); print(rH(rH(a)))", ctrl9));
        menu.addCommand(new RunExampleCommand("10", "int v; v=4; while (v>0) (print(v); v=v-1); print(v)", ctrl10));
        menu.addCommand(new ExitCommand("0", "Exit"));
 
        menu.show();
    }
}
