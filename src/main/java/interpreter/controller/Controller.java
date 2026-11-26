package interpreter.controller;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.stmt.IStmt;
import interpreter.repository.IRepository;

public class Controller {
    private final IRepository repo;
    private boolean displayFlag;

    public Controller(IRepository repo) {
        this(repo, false);
    }

    public Controller(IRepository repo, boolean displayFlag) {
        this.repo = repo;
        this.displayFlag = displayFlag;
    }

    public IRepository getRepo() {
        return repo;
    }

    public boolean isDisplayFlag() {
        return displayFlag;
    }

    public void setDisplayFlag(boolean displayFlag) {
        this.displayFlag = displayFlag;
    }

    public PrgState oneStep(PrgState state) throws MyException {
        var stack = state.getExeStack();
        if (stack.isEmpty()) {
            throw new StatementException("program state stack is empty");
        }
        IStmt crtStmt = stack.pop();
        return crtStmt.execute(state);
    }

    public void allStep() throws MyException {
        PrgState prg = repo.getCrtPrg();
        if (prg == null) {
            throw new StatementException("repository has no program state");
        }
        logAndDisplay(prg);
        while (!prg.getExeStack().isEmpty()) {
            oneStep(prg);
            logAndDisplay(prg); // before the garbage collection
            collectGarbage(prg);
            logAndDisplay(prg); // after the garbage collection
        }
    }

    // writes the state to the log file and, if displayFlag is set, to the console
    private void logAndDisplay(PrgState state) throws MyException {
        repo.logPrgStateExec(state);
        display(state);
    }

    private void collectGarbage(PrgState state) {
        var heap = state.getHeap();
        heap.setContent(GarbageCollector.safeGarbageCollector(
                GarbageCollector.getAddrFromSymTable(state.getSymTable().toMap().values()),
                heap.getContent()));
    }

    public void display(PrgState state) {
        if (displayFlag && state != null) {
            System.out.println(state);
        }
    }
}
