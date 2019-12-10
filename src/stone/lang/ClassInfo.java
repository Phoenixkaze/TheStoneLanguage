package stone.lang;

import stone.lang.ast.ClassBody;
import stone.lang.ast.ClassStatement;

public class ClassInfo {
    protected ClassStatement definition;
    protected Environment environment;
    protected ClassInfo superClass;

    public ClassInfo(ClassStatement statement, Environment environment) throws StoneException {
        definition = statement;
        this.environment = environment;
        Object object = environment.get(statement.superClass()); // Try to get the super class
        if (object == null) {
            superClass = null;
        } else if (object instanceof ClassInfo) {
            superClass = (ClassInfo)object;
        } else {
            throw new StoneException("unknown super class: " + statement.superClass(), statement);
        }
    }

    public String name() {
        return definition.name();
    }

    public ClassInfo superClass() {
        return superClass;
    }

    public ClassBody body() {
        return definition.body();
    }

    public Environment environment() {
        return environment;
    }

    @Override
    public String toString() {
        return "<class " + name() + ">";
    }
}
