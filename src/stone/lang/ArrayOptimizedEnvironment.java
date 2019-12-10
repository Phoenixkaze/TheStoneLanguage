package stone.lang;

public interface ArrayOptimizedEnvironment extends Environment {
    Symbols symbols();
    void put(int nest, int index, Object value) throws StoneException;
    Object get(int nest, int index);
    void putNew(String name, Object value) throws StoneException;
    Environment where(String name) throws StoneException;
}
