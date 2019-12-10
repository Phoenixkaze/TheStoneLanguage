package stone.lang;

public class OptimizedObjectInterpreter extends EnvironmentOptimizedInterpreter {
    public static void main(String[] args) throws StoneException, ParseException {
        run(new ClassParser(), new Native().environment(new ResizableArrayEnvironment()));
    }
}
