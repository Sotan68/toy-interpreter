package interpreter.model.exp;

public enum RelOp {
    LESS("<"),
    LESS_OR_EQUAL("<="),
    EQUAL("=="),
    NOT_EQUAL("!="),
    GREATER(">"),
    GREATER_OR_EQUAL(">=");

    public static final RelOp LT = LESS;
    public static final RelOp LE = LESS_OR_EQUAL;
    public static final RelOp LESS_EQUAL = LESS_OR_EQUAL;
    public static final RelOp EQ = EQUAL;
    public static final RelOp NE = NOT_EQUAL;
    public static final RelOp GT = GREATER;
    public static final RelOp GE = GREATER_OR_EQUAL;
    public static final RelOp GREATER_EQUAL = GREATER_OR_EQUAL;

    private final String symbol;

    RelOp(String symbol) {
        this.symbol = symbol;
    }

    public static RelOp fromString(String op) {
        return switch (op) {
            case "<" -> LESS;
            case "<=" -> LESS_OR_EQUAL;
            case "==" -> EQUAL;
            case "!=" -> NOT_EQUAL;
            case ">" -> GREATER;
            case ">=" -> GREATER_OR_EQUAL;
            default -> throw new IllegalArgumentException("Unknown relational operator: " + op);
        };
    }

    @Override
    public String toString() {
        return symbol;
    }
}

