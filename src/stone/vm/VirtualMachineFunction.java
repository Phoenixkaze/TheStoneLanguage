package stone.vm;

import stone.lang.Environment;
import stone.lang.ast.BlockStatement;
import stone.lang.ast.Function;
import stone.lang.ast.ParameterList;

public class VirtualMachineFunction extends Function {
    protected int entry;

    public VirtualMachineFunction(ParameterList parameters, BlockStatement body, Environment environment, int entry) {
        super(parameters, body, environment);
        this.entry = entry;
    }

    public int entry() {
        return entry;
    }
}
