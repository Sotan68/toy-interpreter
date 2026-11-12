package interpreter.repository;

import interpreter.exceptions.MyException;
import interpreter.model.PrgState;

import java.util.List;

public interface IRepository {
    PrgState getCrtPrg();
    void add(PrgState state);
    List<PrgState> getStates();
    void logPrgStateExec() throws MyException;
    void logPrgStateExec(PrgState state) throws MyException;
}
