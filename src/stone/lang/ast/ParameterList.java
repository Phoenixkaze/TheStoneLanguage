package stone.lang.ast;

import stone.lang.ArrayOptimizedEnvironment;
import stone.lang.Environment;
import stone.lang.StoneException;
import stone.lang.Symbols;
import stone.vm.StoneVirtualMachine;
import stone.vm.VirtualMachineEnvironment;

import java.util.List;

public class ParameterList extends AbstractSyntaxTreeList {
    private int[] offsets = null;

    public ParameterList(List<AbstractSyntaxTree> trees) { super(trees); }
    public String name(int i) { return ((AbstractSyntaxTreeLeaf)child(i)).token().getText(); }
    public int size() { return numberOfChildren(); }
//    public void eval(Environment environment, int index, Object value) throws StoneException {
//        environment.putNew(name(index), value);
//    }

//    public void eval(Environment environment, int index, Object value) throws StoneException {
//        ((ArrayOptimizedEnvironment)environment).put(0, offsets[index], value);
//    }

    public void eval(Environment environment, int index, Object value) {
        StoneVirtualMachine virtualMachine = ((VirtualMachineEnvironment)environment).virtualMachine();
        virtualMachine.stack()[offsets[index]] = value;
    }

    @Override
    public void lookUp(Symbols symbols) throws StoneException {
        int size = size();
        offsets = new int[size];
        for (int i = 0; i < size; i++) {
            offsets[i] = symbols.putNew(name(i));
        }
    }
}
