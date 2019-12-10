package stone.lang;

public class MemberSymbols extends Symbols {
    public static int METHOD = -1;
    public static int FIELD = -2;
    protected int type;
    public MemberSymbols(Symbols outer, int type) {
        super(outer);
        this.type = type;
    }

    @Override
    public Location get(String key, int nest) {
        Integer index = table.get(key);
        if (index == null) {
            if (outer == null) {
                return null;
            } else {
                return outer.get(key, nest);
            }
        } else {
            return new Location(type, index.intValue());
        }
    }

    @Override
    public Location put(String key) {
        Location location = get(key, 0);
        if (location == null) {
            return new Location(type, add(key));
        } else {
            return location;
        }
    }
}
