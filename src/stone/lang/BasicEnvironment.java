package stone.lang;

import java.util.HashMap;

public class BasicEnvironment implements Environment {
    protected HashMap<String, Object> values;
    public BasicEnvironment() {
        values = new HashMap<>();
    }

    @Override
    public void put(String name, Object value) {
        values.put(name, value);
    }

    @Override
    public Object get(String name) {
        return values.get(name);
    }

    @Override
    public void putNew(String name, Object value) {

    }

    @Override
    public void setOuter(Environment environment) {

    }

    @Override
    public Environment where(String name) {
        return null;
    }
}
