package stone.lang;

import stone.lang.ast.BlockStatement;
import stone.lang.ast.ParameterList;

public class OptimizedMethod extends OptimizedFunction {
    OptimizedStoneObject self;
    public OptimizedMethod(ParameterList parameters, BlockStatement body, Environment environment, int memorySize, OptimizedStoneObject self) {
        super(parameters, body, environment, memorySize);
        this.self = self;
    }

    @Override
    public Environment makeEnvironment() throws StoneException {
        ArrayEnvironment environment = new ArrayEnvironment(size, this.environment);
        environment.put(0, 0, self);
        return environment;
    }


}
