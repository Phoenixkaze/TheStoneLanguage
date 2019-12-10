package stone.lang;

import stone.lang.ast.Arguments;
import stone.lang.ast.DefStatement;
import stone.lang.ast.ParameterList;

import static stone.lang.Parser.rule;

public class FunctionParser extends BasicParser {
    public FunctionParser() {
        reserved.add(")");
        primary.repeat(postfix);
        simple.option(args);
        program.insertChoice(def);
    }

    Parser param = rule().identifier(reserved);
    Parser params = rule(ParameterList.class).ast(param).repeat(rule().sep(",").ast(param));
    Parser paramList = rule().sep("(").maybe(params).sep(")");
    Parser def = rule(DefStatement.class).sep("def").identifier(reserved).ast(paramList).ast(block);
    Parser args = rule(Arguments.class).ast(expression).repeat(rule().sep(",").ast(expression));
    Parser postfix = rule().sep("(").maybe(args).sep(")");
}
