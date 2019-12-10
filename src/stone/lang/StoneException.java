package stone.lang;

import stone.lang.ast.AbstractSyntaxTree;

public class StoneException extends Throwable {
    public StoneException(String info) {
        super(info);
    }

    public  StoneException(String info, AbstractSyntaxTree tree) {
        super(info + " " + tree.location());
    }
}
