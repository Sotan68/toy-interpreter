package interpreter.repository;

import interpreter.model.PrgState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Repository implements IRepository {
    private final List<PrgState> states;

    public Repository() {
        this.states = new ArrayList<>();
    }

    public Repository(PrgState state) {
        this.states = new ArrayList<>();
        if (state != null) {
            this.states.add(state);
        }
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
}

