package interpreter.model.stmt;

import interpreter.exceptions.MyException;
import interpreter.exceptions.StatementException;
import interpreter.model.PrgState;
import interpreter.model.exp.IExp;
import interpreter.model.type.IntType;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;
import interpreter.model.value.StringValue;

import java.io.BufferedReader;
import java.io.IOException;

public record ReadFileStmt(IExp exp, String varName) implements IStmt {
    @Override
    public PrgState execute(PrgState state) throws MyException {
        var symTable = state.getSymTable();
        if (!symTable.isDefined(varName)) {
            throw new StatementException("variable " + varName + " was not declared");
        }
        if (!symTable.lookup(varName).getType().equals(new IntType())) {
            throw new StatementException("variable " + varName + " is not of type int");
        }
        IValue val = exp.eval(symTable);
        if (!(val instanceof StringValue(String fileName))) {
            throw new StatementException("file name expression is not a string");
        }
        var fileTable = state.getFileTable();
        StringValue strVal = (StringValue) val;
        if (!fileTable.isDefined(strVal)) {
            throw new StatementException("file " + fileName + " is not opened in FileTable");
        }
        BufferedReader br = fileTable.lookup(strVal);
        try {
            String line = br.readLine();
            int intVal;
            if (line == null || line.trim().isEmpty()) {
                intVal = 0;
            } else {
                try {
                    intVal = Integer.parseInt(line.trim());
                } catch (NumberFormatException e) {
                    throw new StatementException("file content is not a valid integer: " + line);
                }
            }
            //update in symtable
            symTable.update(varName, new IntValue(intVal));
        } catch (IOException e) {
            throw new StatementException("could not read from file " + fileName + ": " + e.getMessage());
        }
        return state;
    }

    @Override
    public String toString() {
        return "readFile(" + exp + ", " + varName + ")";
    }
}

