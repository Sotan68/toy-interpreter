package interpreter.view;

import interpreter.controller.Controller;
import interpreter.exceptions.MyException;
import interpreter.model.PrgState;

public class RunExampleCommand extends Command {
    private final Controller controller;

    public RunExampleCommand(String key, String description, Controller controller) {
        super(key, description);
        this.controller = controller;
    }

    @Override
    public void execute() {
        PrgState prg = controller.getRepo().getCrtPrg();
        if (prg != null && prg.getExeStack().isEmpty()) {
            System.out.println("Example " + getKey() + " has already been run. Final state:");
            System.out.println(prg);
            return;
        }
        try {
            controller.allStep();
            if (controller.isDisplayFlag()) {
                System.out.println("Execution finished. Out: " + prg.getOut());
            } else {
                System.out.println("Final state:");
                System.out.println(prg);
            }
        } catch (MyException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

