package stone.lang.ast;

import stone.lang.Environment;
import stone.lang.OptimizedFunction;
import stone.lang.StoneException;
import stone.lang.Symbols;

import java.util.List;

// Closure
public class Fun extends AbstractSyntaxTreeList {
    private int size = -1;

    public Fun(List<AbstractSyntaxTree> trees) {
        super(trees);
    }

    public static int lookUp(Symbols symbols, ParameterList parameters, BlockStatement body) throws StoneException {
        Symbols newSymbols = new Symbols(symbols);
        parameters.lookUp(newSymbols);
        body.lookUp(newSymbols);
        return newSymbols.size();
    }

    @Override
    public void lookUp(Symbols symbols) throws StoneException {
        size = lookUp(symbols, parameters(), body());
    }

    // @Override
//    public Object eval(Environment environment) {
//        return new Function(parameters(), body(), environment);
//    }

    public Object eval(Environment environment) {
        return new OptimizedFunction(parameters(), body(), environment, size);
    }

    public ParameterList parameters() {
        return (ParameterList)child(0);
    }

    public BlockStatement body() {
        return (BlockStatement)child(1);
    }

    public String toString() {
        return "(fun " + parameters() + " " + body() + ")";
    }
}
