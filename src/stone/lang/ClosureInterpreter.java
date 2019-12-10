package stone.lang;

public class ClosureInterpreter extends BasicInterpreter {
    public static void main(String[] args) throws ParseException, StoneException {
        run(new ClosureParser(), new NestedEnvironment());
    }
}
