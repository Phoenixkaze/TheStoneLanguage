package stone.lang.ast;

import stone.lang.Environment;
import stone.lang.StoneException;
import stone.vm.Code;
import stone.vm.OperationCode;

import java.util.List;

public class WhileStatement extends AbstractSyntaxTreeList {
    public WhileStatement(List<AbstractSyntaxTree> trees) { super(trees); }
    public AbstractSyntaxTree condition() { return child(0); }
    public AbstractSyntaxTree body() { return child(1); }
    public String toString() {
        return "(while " + condition() + " " + body() + ")";
    }
    public Object eval(Environment env) throws StoneException {
        Object result = 0;
        for (;;) {
            Object trees = condition().eval(env);
            if (trees instanceof Integer && ((Integer)trees).intValue() == FALSE)
                return result;
            else
                result = body().eval(env);
        }
    }

    @Override
    public void compile(Code code) throws StoneException {
        int oldRegister = code.getNextRegister();
        code.add(OperationCode.BCONST);
        code.add((byte)0);
        code.add(OperationCode.encodeRegister(code.getNextRegister() + 1));
        code.setNextRegister(code.getNextRegister() + 1);
        int position = code.position();
        condition().compile(code);
        int blockPosition = code.position();
        code.add(OperationCode.IFZERO);
        code.add(OperationCode.encodeRegister(code.getNextRegister() - 1));
        code.setNextRegister(code.getNextRegister() - 1);
        code.add(OperationCode.encodeOffset(0));
        code.setNextRegister(oldRegister);
        body().compile(code);
        int endPosition = code.position();
        code.add(OperationCode.GOTO);
        code.add(OperationCode.encodeShortOffset(position - endPosition));
        code.set(OperationCode.encodeShortOffset(code.position() - blockPosition), blockPosition + 2);
    }
}
