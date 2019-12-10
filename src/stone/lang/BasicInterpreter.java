package stone.lang;

import stone.lang.ast.AbstractSyntaxTree;
import stone.lang.ast.NullStatement;

public class BasicInterpreter {
    public static void main(String[] args) throws ParseException, StoneException {
        run(new BasicParser(), new BasicEnvironment());
    }

    public static void run(BasicParser basicParser, Environment environment) throws ParseException, StoneException {
        Lexer lexer = new Lexer(new CodeDialog());
        while (lexer.peek(0) != Token.EOF) {
            AbstractSyntaxTree tree = basicParser.parse(lexer);
            if (!(tree instanceof NullStatement)) {
                Object result = tree.eval(environment);
                System.out.println("=>" + result);
            }
        }
    }
}
