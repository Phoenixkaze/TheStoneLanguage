package stone.lang.ast;

import stone.lang.Environment;
import stone.lang.StoneException;
import stone.lang.Symbols;
import stone.vm.Code;

import java.util.Iterator;
import java.util.List;

public class AbstractSyntaxTreeList extends AbstractSyntaxTree {
    protected List<AbstractSyntaxTree> children;
    public AbstractSyntaxTreeList(List<AbstractSyntaxTree> list) { children = list; }

    @Override
    public AbstractSyntaxTree child(int i) {
        return children.get(i);
    }

    @Override
    public int numberOfChildren() {
        return children.size();
    }

    @Override
    public Iterator<AbstractSyntaxTree> children() {
        return children.iterator();
    }

    @Override
    public void lookUp(Symbols symbols) throws StoneException {
        for (AbstractSyntaxTree tree: this) {
            tree.lookUp(symbols);
        }
    }

    @Override
    public String location() {
        for (AbstractSyntaxTree tree: children) {
            String s = tree.location();
            if (s != null) {
                return s;
            }
        }
        return null;
    }

    @Override
    public Object eval(Environment environment) throws StoneException {
        throw new StoneException("cannot evaluate: " + toString(), this);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        String separator = "";
        for (AbstractSyntaxTree tree: children) {
            builder.append(separator);
            separator = " ";
            builder.append(tree.toString());
        }
        return builder.append(')').toString();
    }

    public void compile(Code code) throws StoneException {
        for (AbstractSyntaxTree tree : this) {
            tree.compile(code);
        }
    }
}
