package stone.vm;

import stone.lang.ResizableArrayEnvironment;

public class StoneVirtualMachineEnvironment extends ResizableArrayEnvironment implements HeapMemory, VirtualMachineEnvironment {
    protected StoneVirtualMachine virtualMachine;
    protected Code code;

    public StoneVirtualMachineEnvironment(int codeSize, int stackSize, int stringsSize) {
        virtualMachine = new StoneVirtualMachine(codeSize, stackSize, stringsSize, this);
        code = new Code(virtualMachine);
    }

    public StoneVirtualMachine virtualMachine() {
        return virtualMachine;
    }

    public Code code() {
        return code;
    }

    public Object read(int index) {
        return values[index];
    }

    public void write(int index, Object value) {
        values[index] = value;
    }
}
