package stone.lang;

import stone.lang.ast.Fun;

public class ClosureParser extends FunctionParser {
    public ClosureParser() {
        primary.insertChoice(Parser.rule(Fun.class).sep("fun").ast(paramList).ast(block));
    }
}
