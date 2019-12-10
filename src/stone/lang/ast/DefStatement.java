package stone.lang.ast;

import stone.lang.*;
import stone.vm.*;
import stone.vm.OperationCode.*;

import java.util.List;

public class DefStatement extends AbstractSyntaxTreeList {

    protected int index, size;

    public DefStatement(List<AbstractSyntaxTree> trees) {
        super(trees);
    }

    public String name() { return ((AbstractSyntaxTreeLeaf)child(0)).token().getText(); }
    public ParameterList parameters() { return (ParameterList)child(1); }
    public BlockStatement body() { return (BlockStatement)child(2); }
    public String toString() {
        return "(def" + name() + " " + parameters() + " " + body() + ")";
    }
//    public Object eval(Environment environment) throws StoneException {
//        environment.putNew(name(), new Function(parameters(), body(), environment));
//        return name();
//    }

//    public Object eval(Environment environment) throws StoneException {
//        ((ArrayOptimizedEnvironment)environment).put(0, index, new OptimizedFunction(parameters(), body(), environment, size));
//        return name();
//    }

    public Object eval(Environment environment) throws StoneException {
        String functionName = name();
        VirtualMachineEnvironment virtualMachineEnvironment = (VirtualMachineEnvironment)environment;
        Code code = virtualMachineEnvironment.code();
        int entry = code.position();
        compile(code);
        virtualMachineEnvironment.putNew(functionName, new VirtualMachineFunction(parameters(), body(), environment, entry));
        return functionName;
    }

    @Override
    public void compile(Code code) throws StoneException {
        code.setNextRegister(0);
        code.setFrameSize(size + StoneVirtualMachine.SAVEAREASIZE);
        code.add(OperationCode.SAVE);
        code.add(OperationCode.encodeOffset(size));
        body().compile(code);
        code.add(OperationCode.MOVE);
        code.add(OperationCode.encodeRegister(code.getNextRegister() - 1));
        code.add(OperationCode.encodeOffset(0));
        code.add(OperationCode.RESTORE);
        code.add(OperationCode.encodeOffset(size));
        code.add(OperationCode.RETURN);
    }

    @Override
    public void lookUp(Symbols symbols) throws StoneException {
        index = symbols.putNew(name());
        size = Fun.lookUp(symbols, parameters(), body());
    }

    public int locals() {
        return size;
    }

    public void lookUpAsMethod(Symbols symbols) throws StoneException {
        Symbols newSymbols = new Symbols(symbols);
        newSymbols.putNew(SymbolThis.NAME);
        parameters().lookUp(newSymbols);
        size = newSymbols.size();
    }
}
