package stone.lang;

import java.util.Arrays;

public class ResizableArrayEnvironment extends ArrayEnvironment {
    protected Symbols names;

    public ResizableArrayEnvironment() {
        super(10, null);
        names = new Symbols();
    }

    @Override
    public Symbols symbols() {
        return names;
    }

    @Override
    public Object get(String name) throws StoneException {
        Integer i = names.find(name);
        if (i == null) {
            if (outer == null) {
                return null;
            } else {
                return outer.get(name);
            }
        } else {
            return values[i];
        }
    }

    @Override
    public void put(String name, Object value) throws StoneException {
        Environment environment = where(name);
        if (environment == null) {
            environment = this;
        }
        environment.putNew(name, value);
    }

    @Override
    public void put(int nest, int index, Object value) throws StoneException {
        if (nest == 0) {
            assign(index, value);
        } else {
            super.put(nest, index, value);
        }
    }

    @Override
    public void putNew(String name, Object value) throws StoneException {
        assign(names.putNew(name), value);
    }

    @Override
    public Environment where(String name) throws StoneException {
        if (names.find(name) != null) {
            return this;
        } else if (outer == null) {
            return null;
        } else {
            return outer.where(name);
        }
    }

    private void assign(int index, Object value) {
        if (index >= values.length) {
            int newLength = values.length * 2;
            if (index >= newLength) {
                newLength = index + 1;
            }
            values = Arrays.copyOf(values, newLength);
        }
        values[index] = value;
    }
}
