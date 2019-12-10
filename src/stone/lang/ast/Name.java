package stone.lang.ast;

import stone.lang.*;
import stone.vm.Code;
import stone.vm.OperationCode;

public class Name extends AbstractSyntaxTreeLeaf {
    protected static final int UNKNOWN = -1;
    protected int nest, index;

    public Name(Token token) {
        super(token);
        index = UNKNOWN;
    }
    public String name() { return token().getText(); }

//    @Override
//    public Object eval(Environment environment) throws StoneException {
//        Object value = environment.get(name());
//        if (value == null) {
//            throw new StoneException("undefined name: " + name(), this);
//        } else {
//            return value;
//        }
//    }

//    @Override
//    public Object eval(Environment environment) throws StoneException {
//        if (index == UNKNOWN) {
//            return environment.get(name());
//        } else {
//            return ((ArrayOptimizedEnvironment)environment).get(nest, index);
//        }
//    }

    @Override
    public Object eval(Environment environment) throws StoneException {
        if (index == UNKNOWN) {
            return environment.get(name());
        } else if (nest == MemberSymbols.FIELD) {
            return getThis(environment).read(index);
        } else {
            return ((ArrayEnvironment)environment).get(nest, index);
        }
    }

    protected OptimizedStoneObject getThis(Environment environment) {
        return (OptimizedStoneObject) ((ArrayEnvironment)environment).get(0, 0);
    }

    @Override
    public void lookUp(Symbols symbols) throws StoneException {
        Symbols.Location location = symbols.get(name());
        if (location == null) {
            throw new StoneException("undefined name: " + name(), this);
        } else {
            nest = location.nest;
            index = location.index;
        }
    }

    public void lookUpForAssign(Symbols symbols) {
        Symbols.Location location = symbols.put(name());
        nest = location.nest;
        index = location.index;
    }

//    public void evalForAssign(Environment environment, Object value) throws StoneException {
//        if (index == UNKNOWN) {
//            environment.put(name(), value);
//        } else {
//            ((ArrayOptimizedEnvironment)environment).put(nest, index, value);
//        }
//    }

    public void evalForAssign(Environment environment, Object value) throws StoneException {
        if (index == UNKNOWN) {
            environment.put(name(), value);
        } else if (nest == MemberSymbols.FIELD) {
            getThis(environment).write(index, value);
        } else  if (nest == MemberSymbols.METHOD) {
            throw new StoneException("cannot update a method: " + name(), this);
        } else {
            ((ArrayEnvironment)environment).put(nest, index, value);
        }
    }

    public void compile(Code code) throws StoneException {
        if (nest > 0) {
            code.add(OperationCode.GMOVE);
            code.add(OperationCode.encodeShortOffset(code.getNextRegister() + 1));
            code.setNextRegister(code.getNextRegister() + 1);
        } else {
            code.add(OperationCode.MOVE);
            code.add(OperationCode.encodeOffset(index));
            code.add(OperationCode.encodeShortOffset(code.getNextRegister() + 1));
            code.setNextRegister(code.getNextRegister() + 1);
        }
    }

    public void compileAndAssign(Code code) throws StoneException {
        if (nest > 0) {
            code.add(OperationCode.MOVE);
            code.add(OperationCode.encodeRegister(code.getNextRegister() - 1));
            code.setNextRegister(code.getNextRegister() - 1);
            code.add(OperationCode.encodeShortOffset(index));
        } else {
            code.add(OperationCode.MOVE);
            code.add(OperationCode.encodeRegister(code.getNextRegister() - 1));
            code.setNextRegister(code.getNextRegister() - 1);

            code.add(OperationCode.encodeOffset(index));
        }
    }
}
