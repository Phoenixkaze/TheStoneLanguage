/*
package stone.lang;

import javassist.gluonj.Reviser;
import stone.lang.ast.*;

import java.util.List;

@Reviser
public class BasicEvaluator {
    public static final int TRUE = 1;
    public static final int FALSE = 0;

    @Reviser
    public static abstract class AbstractSyntaxTreeEx extends AbstractSyntaxTree {
        public abstract Object eval(Environment environment);
    }

    @Reviser
    public static class AbstractSyntaxTreeListEx extends AbstractSyntaxTreeList {
        public AbstractSyntaxTreeListEx(List<AbstractSyntaxTree> trees) {
            super(trees);
        }
        public Object eval(Environment environment) throws StoneException {
            throw new StoneException("cannot evaluate: " + toString(), this);
        }
    }

    @Reviser
    public static class NumberEx extends NumberLiteral {
        public NumberEx(Token token) {
            super(token);
        }
        public Object eval(Environment environment) {
            return value();
        }
    }

    @Reviser
    public static class StringEx extends StringLiteral {
        public StringEx(Token token) {
            super(token);
        }
        public Object eval(Environment environment) {
            return value();
        }
    }

    @Reviser
    public static class NameEx extends Name {
        public NameEx(Token token) {
            super(token);
        }
        public Object eval(Environment environment) throws StoneException {
            Object value = environment.get(name());
            if (value == null) {
                throw new StoneException("undefined name: " + name(), this);
            } else {
                return value;
            }
        }
    }

    @Reviser
    public static class NegativeEx extends NegativeExpression {
        public NegativeEx(List<AbstractSyntaxTree> trees) {
            super(trees);
        }
        public Object eval(Environment environment) throws StoneException {
            Object value = ((AbstractSyntaxTreeEx)operand()).eval(environment);
            if (value instanceof Integer) {
                return -((Integer) value);
            } else {
                throw new StoneException("bad type for -", this);
            }
        }
    }

    @Reviser 
    public static class BinaryEx extends BinaryExpression {
        public BinaryEx(List<AbstractSyntaxTree> trees) {
            super(trees);
        }
        public Object eval(Environment env) throws StoneException {
            String op = operator();
            if ("=".equals(op)) {
                Object right = ((AbstractSyntaxTreeEx)right()).eval(env);
                return computeAssign(env, right);
            }
            else {
                Object left = ((AbstractSyntaxTreeEx)left()).eval(env);
                Object right = ((AbstractSyntaxTreeEx)right()).eval(env);
                return computeOp(left, op, right);
            }
        }
        protected Object computeAssign(Environment environment, Object rvalue) throws StoneException {
            AbstractSyntaxTree left = left();
            if (left instanceof Name) {
                environment.put(((Name)left).name(), rvalue);
                return rvalue;
            }
            else
                throw new StoneException("bad assignment", this);
        }
        protected Object computeOp(Object left, String op, Object right) throws StoneException {
            if (left instanceof Integer && right instanceof Integer) {
                return computeNumber((Integer)left, op, (Integer)right);
            }
            else
            if (op.equals("+"))
                return String.valueOf(left) + String.valueOf(right);
            else if (op.equals("==")) {
                if (left == null)
                    return right == null ? TRUE : FALSE;
                else
                    return left.equals(right) ? TRUE : FALSE;
            }
            else
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

        @Reviser 
        public static class BlockEx extends BlockStatement {
            public BlockEx(List<AbstractSyntaxTree> trees) { super(trees); }
            public Object eval(Environment env) {
                Object result = 0;
                for (AbstractSyntaxTree t: this) {
                    if (!(t instanceof NullStatement))
                        result = ((AbstractSyntaxTreeEx)t).eval(env);
                }
                return result;
            }
        }
        @Reviser 
        public static class IfEx extends IfStatement {
            public IfEx(List<AbstractSyntaxTree> trees) { super(trees); }
            public Object eval(Environment env) {
                Object trees = ((AbstractSyntaxTreeEx)condition()).eval(env);
                if (trees instanceof Integer && ((Integer)trees).intValue() != FALSE)
                    return ((AbstractSyntaxTreeEx)thenBlock()).eval(env);
                else {
                    AbstractSyntaxTree b = elseBlock();
                    if (b == null)
                        return 0;
                    else
                        return ((AbstractSyntaxTreeEx)b).eval(env);
                }
            }
        }
        @Reviser 
        public static class WhileEx extends WhileStatement {
            public WhileEx(List<AbstractSyntaxTree> trees) { super(trees); }
            public Object eval(Environment env) {
                Object result = 0;
                for (;;) {
                    Object trees = ((AbstractSyntaxTreeEx)condition()).eval(env);
                    if (trees instanceof Integer && ((Integer)trees).intValue() == FALSE)
                        return result;
                    else
                        result = ((AbstractSyntaxTreeEx)body()).eval(env);
                }
            }
        }
    }
}
*/
