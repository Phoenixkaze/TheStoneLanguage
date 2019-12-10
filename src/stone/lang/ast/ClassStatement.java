package stone.lang.ast;

import stone.lang.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class definition
 */
public class ClassStatement extends AbstractSyntaxTreeList {
    public ClassStatement(List<AbstractSyntaxTree> trees) {
        super(trees);
    }

    /**
     * Get class name
     * @return name of the class
     */
    public String name() {
        return ((AbstractSyntaxTreeLeaf)child(0)).token.getText();
    }

    public String superClass() {
        if (numberOfChildren() < 3) {
            return null;
        } else {
            return ((AbstractSyntaxTreeLeaf)child(1)).token.getText();
        }
    }

    public ClassBody body() {
        return (ClassBody)child(numberOfChildren() - 1);
    }

    @Override
    public String toString() {
        String parent = superClass();
        if (parent == null) {
            parent = "*";
        }
        return "(class " + name() + " " + parent + " " + body() + ")";
    }

//    @Override
//    public Object eval(Environment environment) throws StoneException {
//        ClassInfo classInfo = new ClassInfo(this, environment);
//        environment.put(name(), classInfo);
//        return name();
//    }

    @Override
    public Object eval(Environment environment) throws StoneException {
        Symbols methodNames = new MemberSymbols(((ArrayOptimizedEnvironment)environment).symbols(), MemberSymbols.METHOD);
        Symbols fieldNames = new MemberSymbols(((ArrayOptimizedEnvironment)environment).symbols(), MemberSymbols.FIELD);
        OptimizedClassInfo classInfo = new OptimizedClassInfo(this, environment, methodNames, fieldNames);
        ((ArrayOptimizedEnvironment)environment).put(name(), classInfo);
        ArrayList<DefStatement> methods = new ArrayList<>();
        if (classInfo.superClass() != null) {
            classInfo.superClass().copyTo(fieldNames, methodNames, methods);
        }
        Symbols newSymbols = new SymbolThis(fieldNames);
        body().lookUp(newSymbols, methodNames, fieldNames, methods);
        classInfo.setMethods(methods);
        return name();
    }

    public void lookUp() {}
}
