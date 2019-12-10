package stone.lang;

import stone.lang.ast.BlockStatement;
import stone.lang.ast.Function;
import stone.lang.ast.ParameterList;

public class OptimizedFunction extends Function {
    protected int size;
    public OptimizedFunction(ParameterList parameters, BlockStatement body, Environment environment, int memorySize) {
        super(parameters, body, environment);
        size = memorySize;
    }
    @Override
    public Environment makeEnvironment() throws StoneException {
        return new ArrayEnvironment(size, environment);
    }
}
