package stone.lang;

import java.util.HashSet;
import stone.lang.Parser.Operators;
import stone.lang.ast.*;

import static stone.lang.Parser.rule;

public class BasicParser {
    protected HashSet<String> reserved = new HashSet<>();
    private Operators operators = new Parser.Operators();
    private Parser expr0 = rule();
    protected Parser primary = rule(PrimaryExpression.class)
            .or(
                    rule().sep("("). ast(expr0). sep(")"),
                    rule().number(NumberLiteral.class),
                    rule().identifier(Name.class, reserved),
                    rule().string( StringLiteral. class)
            );
    private Parser factor = rule().or(rule(NegativeExpression.class).sep("-").ast(primary), primary);
    protected Parser expression = expr0.expression(BinaryExpression.class, factor, operators);

    private Parser statement0 = rule();
    protected Parser block = rule(BlockStatement.class)
            .sep("{").option(statement0)
            .repeat(rule().sep(";", Token.EOL).option(statement0))
            .sep("}");
    protected Parser simple = rule(PrimaryExpression.class).ast(expression);
    protected Parser statement = statement0.or(
            rule(IfStatement.class).sep("if").ast(expression).ast(block)
            .option(rule().sep("else").ast(block)),
            rule(WhileStatement.class).sep("while").ast(expression).ast(block), simple
    );

    protected Parser program = rule().or(statement, rule(NullStatement.class))
            .sep(";", Token.EOL);

    public BasicParser() {
        reserved.add(";");
        reserved.add("}");
        reserved.add(Token.EOL);

        operators.add("=", 1, Operators.RIGHT);
        operators.add("==", 2, Operators.LEFT);
        operators.add(">", 2, Operators.LEFT);
        operators.add("<", 2, Operators.LEFT);
        operators.add("+", 3, Operators.LEFT);
        operators.add("-", 3, Operators.LEFT);
        operators.add("*", 4, Operators.LEFT);
        operators.add("/", 4, Operators.LEFT);
        operators.add("%", 4, Operators.LEFT);
    }

    public AbstractSyntaxTree parse(Lexer lexer) throws ParseException {
        return program.parse(lexer);
    }
}
