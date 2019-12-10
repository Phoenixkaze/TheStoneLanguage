package stone.lang.ast;

import stone.lang.Environment;
import stone.lang.NativeFunction;
import stone.lang.StoneException;
import stone.vm.*;

import java.util.List;

public class Arguments extends Postfix {
    public Arguments(List<AbstractSyntaxTree> trees) { super(trees); }

//    @Override
//    public Object eval(Environment environment, Object value) throws StoneException {
//        if (!(value instanceof NativeFunction)) {
//            Function function = (Function)value;
//            ParameterList parameters = function.parameters();
//            if (size() != parameters.size()) {
//                throw new StoneException("bad number of arguments", this);
//            }
//            Environment newEnvironment = function.makeEnvironment();
//            int num = 0;
//            for(AbstractSyntaxTree tree: this) {
//                parameters.eval(newEnvironment, num++, tree.eval(environment));
//            }
//            return function.body().eval(newEnvironment);
//        } else {
//            NativeFunction function = (NativeFunction)value;
//            int numberOfParameters = function.getNumberOfParameters();
//            if (size() != numberOfParameters) {
//                throw new StoneException("bad number of parameters", this);
//            }
//            Object[] args = new Object[numberOfParameters];
//            int num = 0;
//            for (AbstractSyntaxTree tree: this) {
//                args[num++] = tree.eval(environment);
//            }
//            return function.invoke(args, this);
//        }
//    }

    @Override
    public Object eval(Environment environment, Object value) throws StoneException {
        if (!(value instanceof VirtualMachineFunction)) {
            throw new StoneException("invalid function", this);
        }
        VirtualMachineFunction function = (VirtualMachineFunction)value;
        ParameterList parameters = function.parameters();
        if (size() != parameters.size()) {
            throw new StoneException("invalid number of arguments", this);
        }
        int num = 0;
        for (AbstractSyntaxTree tree : this) {
            parameters.eval(environment, num++, tree.eval(environment));
        }
        StoneVirtualMachine virtualMachine = ((VirtualMachineEnvironment)environment).virtualMachine();
        virtualMachine.run(function.entry());
        return virtualMachine.stack()[0];
    }

    public int size() {
        return numberOfChildren();
    }

    @Override
    public void compile(Code code) throws StoneException {
        int newOffset = code.getFrameSize();
        int numberOfArgs = 0;
        for (AbstractSyntaxTree tree : this) {
            tree.compile(code);
            code.add(OperationCode.MOVE);
            code.add(OperationCode.encodeRegister(code.getNextRegister() - 1));
            code.setNextRegister(code.getNextRegister() - 1);
            code.add(OperationCode.encodeOffset(newOffset++));
            numberOfArgs++;
        }
        code.add(OperationCode.CALL);
        code.add(OperationCode.encodeRegister(code.getNextRegister() - 1));
        code.setNextRegister(code.getNextRegister() - 1);
        code.add(OperationCode.MOVE);
        code.add(OperationCode.encodeOffset(code.getFrameSize()));
        code.add(OperationCode.encodeRegister(code.getNextRegister()));
        code.setNextRegister(code.getNextRegister() + 1);
    }

}
