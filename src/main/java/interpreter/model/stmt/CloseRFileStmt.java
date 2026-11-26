package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.value.IValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public record CloseRFileStmt(IExp exp) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        IValue val = exp.eval(state.getSymTable(), state.getHeap());
        if (!(val instanceof StringValue(String fileName))) {
            throw new StatementException("file name expression is not a string");
        }
        var fileTable = state.getFileTable();
        StringValue strVal = (StringValue) val;
        if (!fileTable.isDefined(strVal)) {
            throw new StatementException("file " + fileName + " is not opened in FileTable");
        }
        //finds reader in filetable
        BufferedReader br = fileTable.lookup(strVal);
        try {
            br.close();
        } catch (IOException e) {
            throw new StatementException("could not close file " + fileName + ": " + e.getMessage());
        }
        //close & remove it from filetable
        fileTable.remove(strVal);
        return state;
    }

    @Override
    public String toString() {
        return "closeRFile(" + exp + ")";
    }
}

