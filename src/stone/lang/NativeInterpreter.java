package stone.lang;

public class NativeInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws ParseException, StoneException {
        run(new ClosureParser(), new Native().environment(new NestedEnvironment()));
    }
}
