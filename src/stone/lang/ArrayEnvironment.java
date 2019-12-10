package stone.lang;

public class ArrayEnvironment implements ArrayOptimizedEnvironment {
    protected Object[] values;
    protected Environment outer;

    public ArrayEnvironment(int size, Environment outer) {
        values = new Object[size];
        this.outer = outer;
    }

    public Symbols symbols() {
        throw new SecurityException("no symbols");
    }

    public Object get(int nest, int index) {
        if (nest == 0) {
            return values[index];
        } else if (outer == null) {
            return null;
        } else {
            return ((ArrayOptimizedEnvironment)outer).get(nest - 1, index);
        }
    }

    public void put(int nest, int index, Object value) throws StoneException {
        if (nest == 0) {
            values[index] = value;
        } else if (outer == null) {
            throw new StoneException("no outer environment");
        } else {
            ((ArrayOptimizedEnvironment)outer).put(nest - 1, index, value);
        }
    }

    @Override
    public void put(String name, Object value) throws StoneException {
        error(name);
    }

    @Override
    public Object get(String name) throws StoneException {
        error(name);
        return null;
    }

    @Override
    public void putNew(String name, Object value) throws StoneException {
        error(name);
    }

    @Override
    public void setOuter(Environment environment) {
        outer = environment;
    }

    @Override
    public Environment where(String name) throws StoneException {
        error(name);
        return null;
    }

    private void error(String name) throws StoneException {
        throw new StoneException("cannot access by name: " + name);
    }
}
