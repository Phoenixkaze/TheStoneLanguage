package stone.lang;

public class FunctionInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws ParseException, StoneException {
        run(new FunctionParser(), new NestedEnvironment());
    }
}
