package stone.lang.ast;

import stone.lang.Environment;
import stone.lang.StoneException;
import stone.vm.Code;

import java.util.List;

public class PrimaryExpression extends AbstractSyntaxTreeList {
    public PrimaryExpression(List<AbstractSyntaxTree> trees) { super(trees); }

    public  static AbstractSyntaxTree create(List<AbstractSyntaxTree> trees) {
        return trees.size() == 1 ? trees.get(0) : new PrimaryExpression(trees);
    }

    public  AbstractSyntaxTree operand() {
        return child(0);
    }

    public Postfix postfix(int nest) {
        return (Postfix)child(numberOfChildren() - nest - 1);
    }

    public boolean hasPostfix(int nest) {
        return numberOfChildren() - nest > 1;
    }

    public Object eval(Environment environment) throws StoneException {
        return evalSubExpression(environment, 0);
    }

    public  Object evalSubExpression(Environment environment, int nest) throws StoneException {
        if (hasPostfix(nest)) {
            Object target = evalSubExpression(environment, nest + 1);
            return postfix(nest).eval(environment, target);
        } else {
            return operand().eval(environment);
        }
    }

    public void compileSubExpression(Code code, int nest) throws StoneException {
        if (hasPostfix(nest)) {
            compileSubExpression(code, nest + 1);
            postfix(nest).compile(code);
        } else {
            operand().compile(code);
        }
    }
}
