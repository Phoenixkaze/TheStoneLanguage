package stone.lang.ast;

import stone.lang.*;

public class ClassInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws StoneException, ParseException {
        run(new ClassParser(), new Native().environment(new NestedEnvironment()));
    }
}
