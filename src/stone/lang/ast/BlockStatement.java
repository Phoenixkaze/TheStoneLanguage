package stone.lang.ast;

import stone.lang.Environment;
import stone.lang.StoneException;
import stone.vm.Code;
import stone.vm.OperationCode;

import java.util.List;

public class BlockStatement extends AbstractSyntaxTreeList {
    public BlockStatement(List<AbstractSyntaxTree> trees) { super(trees); }
    public Object eval(Environment env) throws StoneException {
        Object result = 0;
        for (AbstractSyntaxTree t: this) {
            if (!(t instanceof NullStatement))
                result = t.eval(env);
        }
        return result;
    }

    @Override
    public void compile(Code code) throws StoneException {
        if (numberOfChildren() > 0) {
            int initialRegister = code.getNextRegister();
            for (AbstractSyntaxTree tree : this) {
                code.setNextRegister(initialRegister);
                tree.compile(code);
            }
        } else {
            code.add(OperationCode.BCONST);
            code.add((byte)0);
            code.add(OperationCode.encodeRegister(code.getNextRegister()));
            code.setNextRegister(code.getNextRegister() + 1);
        }
    }
}
