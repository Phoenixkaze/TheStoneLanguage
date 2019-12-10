package stone.lang;

public class SymbolThis extends Symbols {
    public static final String NAME = "this";
    public SymbolThis(Symbols outer) {
        super(outer);
        add(NAME);
    }

    @Override
    public int putNew(String key) throws StoneException {
        throw new StoneException("fatal");
    }

    @Override
    public Location put(String key) {
        Location location = outer.put(key);
        if (location.nest >= 0) {
            location.nest++;
        }
        return location;
    }
}
