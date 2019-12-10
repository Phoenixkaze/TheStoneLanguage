package stone.lang.ast;

import stone.lang.*;

import java.util.List;

/**
 * The dot operator
 */
public class Dot extends Postfix {
    protected OptimizedClassInfo classInfo = null;
    protected boolean isField;
    protected int index;

    public Dot(List<AbstractSyntaxTree> trees) {
        super(trees);
    }

    public String name() {
        return ((AbstractSyntaxTreeLeaf)child(0)).token().getText();
    }

    @Override
    public String toString() {
        return "." + name();
    }

//    @Override
//    public Object eval(Environment environment, Object value) throws StoneException {
//        String member = name();
//        if (value instanceof ClassInfo) {
//            if ("new".equals(member)) {
//                ClassInfo classInfo = (ClassInfo)value;
//                NestedEnvironment nestedEnvironment = new NestedEnvironment(classInfo.environment());
//                StoneObject object = new StoneObject(nestedEnvironment);
//                nestedEnvironment.putNew("this", object);
//                initObject(classInfo, nestedEnvironment);
//                return object;
//            }
//        } else if (value instanceof StoneObject) {
//            try {
//                return ((StoneObject)value).read(member);
//            } catch (StoneObject.AccessException e) {}
//        }
//        throw new StoneException("bad member access: " + member, this);
//    }

    @Override
    public Object eval(Environment environment, Object value) throws StoneException {
        String member = name();
        if (value instanceof OptimizedClassInfo) {
            if ("new".equals(member)) {
                OptimizedClassInfo classInfo = (OptimizedClassInfo)value;
                ArrayEnvironment newEnvironment = new ArrayEnvironment(1, classInfo.environment());
                OptimizedStoneObject object = new OptimizedStoneObject(classInfo, classInfo.size());
                newEnvironment.put(0, 0, object);
                initObject(classInfo, object, newEnvironment);
                return object;
            }
        } else if (value instanceof OptimizedStoneObject) {
            OptimizedStoneObject target = (OptimizedStoneObject)value;
            if (target.classInfo() != classInfo) {
                updateCache(target);
            }
            if (isField) {
                return target.read(index);
            } else {
                return target.method(index);
            }
//            try {
//                return ((OptimizedStoneObject)value).read(member);
//            } catch (OptimizedStoneObject.AccessException e) {}
        }
        throw new StoneException("invalid member: " + member, this);
    }

    protected void updateCache(OptimizedStoneObject target) throws StoneException {
        String member = name();
        classInfo = target.classInfo();
        Integer i = classInfo.fieldIndex(member);
        if (i != null) {
            isField = true;
            index = i;
            return;
        }
        i = classInfo.methodIndex(member);
        if (i != null) {
            isField = false;
            index = i;
            return;
        }
        throw new StoneException("bad member access: " + member, this);
    }

    /**
     * When used like Class.new
     * @param classInfo the class
     * @param object
     * @param environment
     * @throws StoneException
     */
//    protected void initObject(ClassInfo classInfo, OptimizedStoneObject object, Environment environment) throws StoneException {
//        if (classInfo.superClass() != null) {
//            initObject(classInfo.superClass(), object, environment);
//        }
//        classInfo.body().eval(environment);
//    }

    protected void initObject(OptimizedClassInfo classInfo, OptimizedStoneObject object, Environment environment) throws StoneException {
        if (classInfo.superClass() != null) {
            initObject(classInfo.superClass(), object, environment);
        }
        classInfo.body().eval(environment);
    }
}
