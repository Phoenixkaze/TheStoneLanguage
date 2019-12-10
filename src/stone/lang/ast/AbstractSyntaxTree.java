package stone.lang.ast;

import jdk.jshell.spi.ExecutionControl;
import stone.lang.Environment;
import stone.lang.StoneException;
import stone.lang.Symbols;
import stone.vm.Code;

import java.util.Iterator;

public abstract class AbstractSyntaxTree implements Iterable<AbstractSyntaxTree> {
    public static final int TRUE = 1;
    public static final int FALSE = 0;
    public abstract Object eval(Environment environment) throws StoneException;
    public abstract AbstractSyntaxTree child(int i);
    public abstract int numberOfChildren();
    public abstract Iterator<AbstractSyntaxTree> children();
    public abstract String location();
    public void lookUp(Symbols symbols) throws StoneException {}

    @Override
    public Iterator<AbstractSyntaxTree> iterator() {
        return children();
    }

    public void compile(Code code) throws StoneException { }
}
