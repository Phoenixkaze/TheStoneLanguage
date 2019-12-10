package stone;

import stone.lang.*;
import stone.vm.StoneVirtualMachineEnvironment;

public class VirtualMachineInterpreter extends EnvironmentOptimizedInterpreter {
    public static void main(String[] args) throws StoneException, ParseException {
        run(new FunctionParser(), new Native().environment(new StoneVirtualMachineEnvironment(100000, 100000, 1000)));
    }
}
