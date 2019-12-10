package stone.lang.ast;

import stone.lang.Environment;
import stone.lang.StoneException;

import java.util.List;

public class ArrayRef extends Postfix {
    public ArrayRef(List<AbstractSyntaxTree> trees) {
        super(trees);
    }

    public AbstractSyntaxTree index() {
        return child(0);
    }

    @Override
    public String toString() {
        return "[" + index() + "]";
    }

    @Override
    public Object eval(Environment environment, Object value) throws StoneException {
        if (value instanceof Object[]) {
            Object index = index().eval(environment);
            if (index instanceof Integer) {
                return ((Object[])value)[(Integer)index];
            }
        }
        throw new StoneException("invalid array index", this);
    }
}
