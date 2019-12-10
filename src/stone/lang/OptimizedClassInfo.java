package stone.lang;

import stone.lang.ast.ClassStatement;
import stone.lang.ast.DefStatement;

import java.util.ArrayList;

public class OptimizedClassInfo extends ClassInfo {
    protected Symbols methods, fields;
    protected DefStatement[] methodDefinitions;
    public OptimizedClassInfo(ClassStatement statement, Environment environment, Symbols methods, Symbols fields) throws StoneException {
        super(statement, environment);
        this.methods = methods;
        this.fields = fields;
        methodDefinitions = null;
    }

    public int size() {
        return fields.size();
    }

    @Override
    public OptimizedClassInfo superClass() {
        return (OptimizedClassInfo)superClass;
    }


    public void copyTo(Symbols fields, Symbols methods, ArrayList<DefStatement> definitions) {
        fields.append(this.fields);
        methods.append(this.methods);
        for (DefStatement definition: methodDefinitions) {
            definitions.add(definition);
        }
    }

    public Integer fieldIndex(String name) {
        return fields.find(name);
    }

    public Integer methodIndex(String name) {
        return methods.find(name);
    }

    public Object method(OptimizedStoneObject self, int index) {
        DefStatement definition = methodDefinitions[index];
        return new OptimizedMethod(definition.parameters(), definition.body(), environment(), definition.locals(), self);
    }

    public void setMethods(ArrayList<DefStatement> methods) {
        methodDefinitions = methods.toArray(new DefStatement[methods.size()]);
    }
}
