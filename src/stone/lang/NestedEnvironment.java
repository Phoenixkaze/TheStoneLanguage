package stone.lang;

import java.util.HashMap;

public class NestedEnvironment implements Environment {
    protected HashMap<String, Object> values;
    protected Environment outer;

    public NestedEnvironment() { this(null); }
    public NestedEnvironment(Environment environment) {
        values = new HashMap<>();
        outer = environment;
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
    public Object get(String name) throws StoneException {
        Object v = values.get(name);
        if (v == null && outer != null) {
            return  outer.get(name);
        } else {
            return v;
        }
    }

    public void setOuter(Environment environment) {
        outer = environment;
    }

    public void putNew(String name, Object value) {
        values.put(name, value);
    }

    public Environment where(String name) throws StoneException {
        if (values.get(name) != null) {
            return this;
        } else if (outer == null) {
            return null;
        } else {
            return outer.where(name);
        }
    }
}
