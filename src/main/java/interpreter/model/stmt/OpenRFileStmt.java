package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public record OpenRFileStmt(IExp exp) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue val = exp.eval(state.getSymTable(), state.getHeap());
        if (!(val instanceof StringValue(String fileName))) {
            throw new StatementException("file name expression is not a string");
        }
        var fileTable = state.getFileTable();
        StringValue strVal = (StringValue) val;
        if (fileTable.isDefined(strVal)) {
            throw new StatementException("file " + fileName + " is already opened");
        }
        try { //creare buffedraeder si introducere in filetable
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            fileTable.put(strVal, br);
        } catch (IOException e) {
            throw new StatementException("could not open file " + fileName + ": " + e.getMessage());
        }
        return state;
    }

    @Override
    public String toString() {
        return "openRFile(" + exp + ")";
    }
}

