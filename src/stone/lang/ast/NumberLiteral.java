package stone.lang.ast;

import stone.lang.Environment;
import stone.lang.StoneException;
import stone.lang.Token;
import stone.lang.ast.AbstractSyntaxTreeLeaf;
import stone.vm.Code;
import stone.vm.OperationCode;

public class NumberLiteral extends AbstractSyntaxTreeLeaf {
    public NumberLiteral(Token token) { super(token); }
    public int value() throws StoneException { return token().getNumber(); }
    public Object eval(Environment environment) throws StoneException {
        return value();
    }

    @Override
    public void compile(Code code) throws StoneException {
        int value = value();
        if (Byte.MIN_VALUE <= value && value <= Byte.MAX_VALUE) {
            code.add(OperationCode.BCONST);
            code.add((byte)value);
        } else {
            code.add(OperationCode.ICONST);
            code.add(value);
        }
        var nextRegister = code.getNextRegister();
        code.add(OperationCode.encodeRegister(nextRegister++));
        code.setNextRegister(nextRegister);
    }
}
