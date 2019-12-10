package stone.lang.ast;

import jdk.jshell.spi.ExecutionControl;
import stone.lang.Environment;
import stone.lang.StoneException;
import stone.lang.Token;

import java.util.ArrayList;
import java.util.Iterator;

public class AbstractSyntaxTreeLeaf extends AbstractSyntaxTree {
    private static ArrayList<AbstractSyntaxTree> empty = new ArrayList<>();
    protected Token token;
    public AbstractSyntaxTreeLeaf(Token token) { this.token = token; }

    @Override
    public Object eval(Environment environment) throws StoneException {
        return null;
    }

    @Override
    public AbstractSyntaxTree child(int i) {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public int numberOfChildren() {
        return 0;
    }

    @Override
    public Iterator<AbstractSyntaxTree> children() {
        return empty.iterator();
    }

    @Override
    public String location() {
        return "at line " + token.getLineNumber();
    }

    public Token token() { return token; }
}
