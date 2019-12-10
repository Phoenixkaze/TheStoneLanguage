package stone.lang;

import stone.lang.ast.ClassInterpreter;

public class ArrayInterpreter extends ClassInterpreter {
    public static void main(String[] args) throws StoneException, ParseException {
        run(new ArrayParser(), new Native().environment(new NestedEnvironment()));
    }
}
