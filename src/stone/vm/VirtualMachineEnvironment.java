package stone.vm;

import stone.lang.ArrayOptimizedEnvironment;

public interface VirtualMachineEnvironment extends ArrayOptimizedEnvironment {
    StoneVirtualMachine virtualMachine();
    Code code();
}
