package stone.lang;

import stone.lang.ast.AbstractSyntaxTree;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NativeFunction {
    protected Method method;
    protected String name;
    protected int numberOfParameters;

    public NativeFunction(String name, Method method) {
        this.name = name;
        this.method = method;
        numberOfParameters = method.getParameterTypes().length;
    }

    @Override
    public String toString() {
        return "<native:" + hashCode() + ">";
    }

    public int getNumberOfParameters() {
        return numberOfParameters;
    }

    public Object invoke(Object[] args, AbstractSyntaxTree tree) throws StoneException {
        try {
            return method.invoke(null, args);
        } catch (Exception e) {
            throw new StoneException("bad native function call:" + name, tree);
        }
    }
}
