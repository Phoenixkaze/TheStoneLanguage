package stone.lang;

import java.util.HashMap;

public class Symbols {
    public static class Location {
        public int nest, index;
        public Location(int nest, int index) {
            this.nest = nest;
            this.index = index;
        }
    }

    protected Symbols outer;
    protected HashMap<String, Integer> table;

    public Symbols() {
        this(null);
    }
    public Symbols(Symbols outer) {
        this.outer = outer;
        table = new HashMap<>();
    }

    public int size() {
        return table.size();
    }

    public void append(Symbols symbols) {
        table.putAll(symbols.table);
    }

    public Integer find(String key) {
        return table.get(key);
    }

    public Location get(String key) {
        return get(key, 0);
    }

    public Location get(String key, int nest) {
        Integer index = table.get(key);
        if (index == null) {
            if (outer == null) {
                return null;
            } else {
                return outer.get(key, nest + 1);
            }
        } else {
            return new Location(nest, index.intValue());
        }
    }

    public int putNew(String key) throws StoneException {
        Integer i = find(key);
        if (i == null) {
            return add(key);
        } else {
            return i;
        }
    }

    public Location put(String key) {
        Location location = get(key, 0);
        if (location == null) {
            return new Location(0, add(key));
        } else {
            return location;
        }
    }

    protected int add(String key) {
        int i = table.size();
        table.put(key, i);
        return i;
    }
}
