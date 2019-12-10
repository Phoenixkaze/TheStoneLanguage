package stone.lang.ast;

import stone.lang.Environment;
import stone.lang.StoneException;
import stone.vm.Code;
import stone.vm.OperationCode;

import java.util.List;

public class NegativeExpression extends AbstractSyntaxTreeList {
    public NegativeExpression(List<AbstractSyntaxTree> trees) { super(trees); }
    public AbstractSyntaxTree operand() { return child(0); }
    public String toString() {
        return "-" + operand();
    }
    public Object eval(Environment environment) throws StoneException {
        Object value = operand().eval(environment);
        if (value instanceof Integer) {
            return -((Integer) value);
        } else {
            throw new StoneException("bad type for -", this);
        }
    }

    @Override
    public void compile(Code code) throws StoneException {
        operand().compile(code);
        code.add(OperationCode.NEG);
        code.add(OperationCode.encodeRegister(code.getNextRegister() - 1));
        code.setNextRegister(code.getNextRegister() - 1);
    }
}
