package stone.lang.ast;

import stone.lang.Environment;
import stone.lang.StoneException;
import stone.lang.Symbols;

import java.util.ArrayList;
import java.util.List;

public class ClassBody extends AbstractSyntaxTreeList {
    public ClassBody(List<AbstractSyntaxTree> trees) {
        super(trees);
    }

    @Override
    public Object eval(Environment environment) throws StoneException {
        for (AbstractSyntaxTree tree: this) {
            if (!(tree instanceof DefStatement)) {
                tree.eval(environment);
            }
        }
        return null;
    }

    public void lookUp(Symbols symbols, Symbols methodNames, Symbols fieldNames, ArrayList<DefStatement> methods) throws StoneException {
        for (AbstractSyntaxTree tree : this) {
            if (tree instanceof DefStatement) {
                DefStatement def = (DefStatement)tree;
                int oldSize = methodNames.size();
                int i = methodNames.putNew(def.name());
                if (i >= oldSize) {
                    methods.add(def);
                } else {
                    methods.set(i, def);
                }
                def.lookUpAsMethod(fieldNames);
            } else {
                tree.lookUp(symbols);
            }
        }
    }
}
