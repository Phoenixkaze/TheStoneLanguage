package stone.lang;

public interface Environment {
    void put(String name, Object value) throws StoneException;
    Object get(String name) throws StoneException;
    void putNew(String name, Object value) throws StoneException;
    void setOuter(Environment environment);

    Environment where(String name) throws StoneException;
}

