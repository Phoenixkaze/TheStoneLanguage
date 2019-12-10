package stone.lang.ast;

import stone.lang.Environment;
import stone.lang.StoneException;
import stone.lang.Token;
import stone.vm.Code;
import stone.vm.OperationCode;

public class StringLiteral extends AbstractSyntaxTreeLeaf {
    public StringLiteral(Token token) { super(token); }
    public String value() { return token().getText(); }
    public Object eval(Environment environment) {
        return value();
    }

    public void compile(Code code) throws StoneException {
        int i = code.record(value());
        code.add(OperationCode.SCONST);
        code.add(OperationCode.encodeShortOffset(i));
        code.add(OperationCode.encodeRegister(code.getNextRegister() + 1));
        code.setNextRegister(code.getNextRegister() + 1);
    }
}
