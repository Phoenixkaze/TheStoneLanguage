package stone.lang.ast;

import stone.lang.Environment;
import stone.lang.StoneException;

import java.util.List;

public abstract class Postfix extends AbstractSyntaxTreeList {
    public Postfix(List<AbstractSyntaxTree> trees) {
        super(trees);
    }
    public abstract Object eval(Environment environment, Object value) throws StoneException;
}
