package stone.lang.ast;

import stone.lang.Environment;
import stone.lang.NestedEnvironment;
import stone.lang.StoneException;

public class Function {
    protected ParameterList parameters;
    protected BlockStatement body;
    protected Environment environment;

    public Function(ParameterList parameters, BlockStatement body, Environment environment) {
        this.parameters = parameters;
        this.body = body;
        this.environment =environment;
    }

    public ParameterList parameters() {
        return parameters;
    }

    public BlockStatement body() {
        return body;
    }

    public Environment makeEnvironment() throws StoneException {
        return new NestedEnvironment(environment);
    }

    @Override
    public String toString() {
        return "<fun:" + hashCode() + ">";
    }
}
