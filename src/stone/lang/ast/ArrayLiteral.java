package stone.lang.ast;

import stone.lang.Environment;
import stone.lang.StoneException;

import java.util.List;

public class ArrayLiteral extends AbstractSyntaxTreeList {
    public ArrayLiteral(List<AbstractSyntaxTree> list) {
        super(list);
    }

    public int size() {
        return numberOfChildren();
    }

    @Override
    public Object eval(Environment environment) throws StoneException {
        int size = numberOfChildren();
        Object[] res = new Object[size];
        int i = 0;
        for (AbstractSyntaxTree tree:this) {
            res[i++] = ((AbstractSyntaxTree)tree).eval(environment);
        }
        return res;
    }
}
