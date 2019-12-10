package stone.lang.ast;

import stone.lang.Environment;
import stone.lang.StoneException;
import stone.vm.Code;
import stone.vm.OperationCode;

import java.util.List;

public class IfStatement extends AbstractSyntaxTreeList {
    public IfStatement(List<AbstractSyntaxTree> trees) { super(trees); }
    public AbstractSyntaxTree condition() { return child(0); }
    public AbstractSyntaxTree thenBlock() { return child(1); }
    public AbstractSyntaxTree elseBlock() {
        return numberOfChildren() > 2 ? child(2) : null;
    }
    public String toString() {
        return "(if" + condition() + " " + thenBlock() + " else " + elseBlock() + ")";
    }
    public Object eval(Environment env) throws StoneException {
        Object trees = condition().eval(env);
        if (trees instanceof Integer && ((Integer)trees).intValue() != FALSE)
            return thenBlock().eval(env);
        else {
            AbstractSyntaxTree b = elseBlock();
            if (b == null)
                return 0;
            else
                return b.eval(env);
        }
    }

    @Override
    public void compile(Code code) throws StoneException {
        condition().compile(code);
        int position = code.position();
        code.add(OperationCode.IFZERO);
        code.setNextRegister(code.getNextRegister() - 1);
        code.add(OperationCode.encodeRegister(code.getNextRegister() - 1));
        code.add(OperationCode.encodeShortOffset(0));
        int oldRegister = code.getNextRegister();
        thenBlock().compile(code);
        int thenPosition = code.position();
        code.add(OperationCode.GOTO);
        code.add(OperationCode.encodeShortOffset(0));
        code.set(OperationCode.encodeShortOffset(code.position() - position), position + 2);
        AbstractSyntaxTree elseBlock = elseBlock();
        code.setNextRegister(oldRegister);
        if (elseBlock != null) {
            elseBlock.compile(code);
        } else {
            code.add(OperationCode.BCONST);
            code.add((byte)0);
            code.add(OperationCode.encodeRegister(code.getNextRegister()));
            code.setNextRegister(code.getNextRegister() + 1);
        }
        code.set(OperationCode.encodeShortOffset(code.position() - thenPosition), thenPosition + 1);
    }
}
