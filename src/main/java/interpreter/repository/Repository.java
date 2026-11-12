package interpreter.repository;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Repository implements IRepository {
    private final List<PrgState> states;
    private final String logFilePath;

    public Repository() {
        this(null, "");
    }

    public Repository(String logFilePath) {
        this(null, logFilePath);
    }

    public Repository(PrgState state) {
        this(state, "");
    }

    public Repository(PrgState state, String logFilePath) {
        this.states = new ArrayList<>();
        if (state != null) {
            this.states.add(state);
        }
        this.logFilePath = logFilePath != null ? logFilePath : "";
    }

    @Override
    public PrgState getCrtPrg() {
        if (states.isEmpty()) {
            return null;
        }
        return states.get(0);
    }

    @Override
    public void add(PrgState state) {
        states.add(state);
    }

    @Override
    public List<PrgState> getStates() {
        return Collections.unmodifiableList(states);
    }

    public String getLogFilePath() {
        return logFilePath;
    }

    @Override
    public void logPrgStateExec() throws MyException {
        logPrgStateExec(getCrtPrg());
    }

    @Override
    public void logPrgStateExec(PrgState state) throws MyException {
        if (logFilePath.isEmpty() || state == null) {
            return;
        }
        try (PrintWriter logFile = new PrintWriter(new BufferedWriter(new FileWriter(logFilePath, true)))) {
            logFile.println(state);
        } catch (IOException e) {
            throw new StatementException("could not write to log file: " + e.getMessage());
        }
    }
}
