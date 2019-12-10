package stone.lang;

import stone.lang.ast.ArrayLiteral;
import stone.lang.ast.ArrayRef;

public class ArrayParser extends FunctionParser {
    Parser elements = Parser.rule(ArrayLiteral.class).ast(expression).repeat(Parser.rule().sep(",").ast(expression));

    public ArrayParser() {
        reserved.add("]");
        primary.insertChoice(Parser.rule().sep("[").maybe(elements).sep("]"));
        postfix.insertChoice(Parser.rule(ArrayRef.class).sep("[").ast(expression).sep("]"));
    }
}
