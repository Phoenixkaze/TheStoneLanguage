package stone.lang;

import stone.lang.ast.ClassBody;
import stone.lang.ast.ClassStatement;
import stone.lang.ast.Dot;

public class ClassParser extends ClosureParser {
    Parser member = Parser.rule().or(def, simple);
    Parser classBody = Parser.rule(ClassBody.class).sep("{")
            .option(member).repeat(Parser.rule().sep(";", Token.EOL).option(member))
            .sep("}");
    Parser defClass = Parser.rule(ClassStatement.class).sep("class").identifier(reserved)
            .option(Parser.rule().sep("extends").identifier(reserved)).ast(classBody);

    public ClassParser() {
        postfix.insertChoice(Parser.rule(Dot.class).sep(".").identifier(reserved));
        program.insertChoice(defClass);
    }
}
