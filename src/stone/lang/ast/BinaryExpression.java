package stone.lang.ast;

import stone.lang.*;
import stone.vm.Code;
import stone.vm.OperationCode;

import java.util.List;

public class BinaryExpression extends AbstractSyntaxTreeList {
    protected OptimizedClassInfo classInfo = null;
    protected int index;

    public BinaryExpression(List<AbstractSyntaxTree> children) {
        super(children);
    }

    public AbstractSyntaxTree left() {
        return child(0);
    }

    public String operator() {
        return ((AbstractSyntaxTreeLeaf) child(1)).token().getText();
    }

    public AbstractSyntaxTree right() {
        return child(2);
    }

    @Override
    public Object eval(Environment env) throws StoneException {
        String op = operator();
        if ("=".equals(op)) {
            Object right = (right()).eval(env);
            return computeAssign(env, right);
        } else {
            Object left = left().eval(env);
            Object right = right().eval(env);
            return computeOp(left, op, right);
        }
    }

    public void lookUp(Symbols symbols) throws StoneException {
        AbstractSyntaxTree left = left();
        if ("=".equals(operator())) {
            if (left instanceof Name) {
                ((Name) left).lookUpForAssign(symbols);
                right().lookUp(symbols);
                return;
            }
        }
        left.lookUp(symbols);
        right().lookUp(symbols);
    }

    protected Object computeAssign(Environment environment, Object rvalue) throws StoneException {
        AbstractSyntaxTree left = left();
        if (left instanceof PrimaryExpression) {
            PrimaryExpression primaryExpression = (PrimaryExpression)left;

            // If it's accessing an array
            if (primaryExpression.hasPostfix(0) && primaryExpression.postfix(0) instanceof ArrayRef) {
                Object object = ((PrimaryExpression) left).evalSubExpression(environment, 1);
                if (object instanceof Object[]) {
                    ArrayRef ref = (ArrayRef)primaryExpression.postfix(0);
                    Object index = ref.index().eval(environment);
                    if (index instanceof Integer) {
                        ((Object[])object)[(Integer)index] = rvalue;
                        return rvalue;
                    }
                }
                throw new StoneException("invalid array access");
            }

            // if it's a dot member access
//            if (primaryExpression.hasPostfix(0) && primaryExpression.postfix(0) instanceof Dot) {
//                Object object = ((PrimaryExpression)left).evalSubExpression(environment, 1);
//                if (object instanceof StoneObject) {
//                    return setField((StoneObject)object, (Dot)primaryExpression.postfix(0), rvalue);
//                }
//            }

            if (left instanceof PrimaryExpression) {
                if (primaryExpression.hasPostfix(0) && primaryExpression.postfix(0) instanceof Dot) {
                    Object t = ((PrimaryExpression)left).evalSubExpression(environment, 1);
                    if (t instanceof OptimizedStoneObject) {
                        return setField((OptimizedStoneObject)t, (Dot)primaryExpression.postfix(0), rvalue);
                    }
                }
            }
        }
        if (left instanceof Name) {
//            environment.put(((Name) left).name(), rvalue);
            ((Name)left).evalForAssign(environment, rvalue);
            return rvalue;
        } else
            throw new StoneException("bad assignment", this);
    }

//    protected Object setField(StoneObject object, Dot expression, Object value) throws StoneException {
//        String name = expression.name();
//        try {
//            object.write(name, value);
//            return value;
//        } catch (StoneObject.AccessException e) {
//            throw new StoneException("bad member access " + location() + ": " + name);
//        }
//    }

//    protected Object setField(OptimizedStoneObject object, Dot dotExpression, Object rvalue) throws StoneException {
//        String name = dotExpression.name();
//        try {
//            object.write(name, rvalue);
//            return rvalue;
//        } catch (OptimizedStoneObject.AccessException e) {
//            throw new StoneException("bad member access: " + name, this);
//        }
//    }

    protected Object setField(OptimizedStoneObject object, Dot dotExpression, Object rvalue) throws StoneException {
        if (object.classInfo() != classInfo) {
            String member = dotExpression.name();
            classInfo = object.classInfo();
            Integer i = classInfo.fieldIndex(member);
            if (i == null) {
                throw new StoneException("bad member access: " + member, this);
            }
            index = i;
        }
        object.write(index, rvalue);
        return rvalue;
    }

    protected Object computeOp(Object left, String op, Object right) throws StoneException {
        if (left instanceof Integer && right instanceof Integer) {
            return computeNumber((Integer) left, op, (Integer) right);
        } else if (op.equals("+"))
            return String.valueOf(left) + String.valueOf(right);
        else if (op.equals("==")) {
            if (left == null)
                return right == null ? TRUE : FALSE;
            else
                return left.equals(right) ? TRUE : FALSE;
        } else
            throw new StoneException("bad type", this);
    }

    protected Object computeNumber(Integer left, String op, Integer right) throws StoneException {
        int a = left.intValue();
        int b = right.intValue();
        if (op.equals("+"))
            return a + b;
        else if (op.equals("-"))
            return a - b;
        else if (op.equals("*"))
            return a * b;
        else if (op.equals("/"))
            return a / b;
        else if (op.equals("%"))
            return a % b;
        else if (op.equals("=="))
            return a == b ? TRUE : FALSE;
        else if (op.equals(">"))
            return a > b ? TRUE : FALSE;
        else if (op.equals("<"))
            return a < b ? TRUE : FALSE;
        else
            throw new StoneException("bad operator", this);
    }

    @Override
    public void compile(Code code) throws StoneException {
        String operator = operator();
        if (operator.equals("=")) {
            AbstractSyntaxTree left = left();
            if (left instanceof Name) {
                right().compile(code);
                left.compile(code);
            } else {
                throw new StoneException("invalid assignment", this);
            }
        } else {
            left().compile(code);
            right().compile(code);
            code.add(getOperationCode(operator));
            code.add(OperationCode.encodeRegister(code.getNextRegister() - 2));
            code.add(OperationCode.encodeRegister(code.getNextRegister() - 1));
            code.setNextRegister(code.getNextRegister() - 1);
        }
    }

    protected byte getOperationCode(String operator) throws StoneException {
        if (operator.equals("+")) {
            return OperationCode.ADD;
        } else if (operator.equals("-")) {
            return OperationCode.SUB;
        } else if (operator.equals("*")) {
            return OperationCode.MUL;
        } else if (operator.equals("/")) {
            return OperationCode.DIV;
        } else if (operator.equals("%")) {
            return OperationCode.REM;
        } else if (operator.equals("==")) {
            return OperationCode.EQUAL;
        } else if (operator.equals(">")) {
            return OperationCode.MORE;
        } else if (operator.equals("<")) {
            return OperationCode.LESS;
        } else {
            throw new StoneException("invalid operator", this);
        }
    }
}
