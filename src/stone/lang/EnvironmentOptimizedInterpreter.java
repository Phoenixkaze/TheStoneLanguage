package stone.lang;

import stone.lang.ast.AbstractSyntaxTree;
import stone.lang.ast.NullStatement;
import stone.lang.ast.OptimizedAbstractSyntaxTree;

public class EnvironmentOptimizedInterpreter {
    public static void main(String[] args) throws StoneException, ParseException {
        run(new ClosureParser(), new Native().environment(new ResizableArrayEnvironment()));
    }

    public static void run(BasicParser basicParser, Environment environment) throws StoneException, ParseException {
        Lexer lexer = new Lexer(new CodeDialog());
        while (lexer.peek(0) != Token.EOF) {
            AbstractSyntaxTree tree = basicParser.parse(lexer);
            if (!(tree instanceof NullStatement)) {
                tree.lookUp(((ArrayOptimizedEnvironment)environment).symbols());
                Object result = tree.eval(environment);
                System.out.println("=> " + result);
            }
        }
    }
}
