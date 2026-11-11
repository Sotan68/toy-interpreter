package interpreter.model.exp;

import interpreter.exceptions.ExpressionException;
import interpreter.exceptions.MyException;
import interpreter.model.adt.IDictionary;
import interpreter.model.value.BoolValue;
import interpreter.model.value.IValue;
import interpreter.model.value.IntValue;

public record RelationalExp(RelOp op, IExp left, IExp right) implements IExp {
    public RelationalExp(String op, IExp left, IExp right) {
        this(RelOp.fromString(op), left, right);
    }

    @Override
    public IValue eval(IDictionary<String, IValue> symTable) throws MyException {
        if (!(left.eval(symTable) instanceof IntValue(int n1))) {
            throw new ExpressionException("first operand is not an integer");
        }
        if (!(right.eval(symTable) instanceof IntValue(int n2))) {
            throw new ExpressionException("second operand is not an integer");
        }
        return switch (op) {
            case LESS -> new BoolValue(n1 < n2);
            case LESS_OR_EQUAL -> new BoolValue(n1 <= n2);
            case EQUAL -> new BoolValue(n1 == n2);
            case NOT_EQUAL -> new BoolValue(n1 != n2);
            case GREATER -> new BoolValue(n1 > n2);
            case GREATER_OR_EQUAL -> new BoolValue(n1 >= n2);
        };
    }

    @Override
    public String toString() {
        return "(" + left + " " + op + " " + right + ")";
    }
}

