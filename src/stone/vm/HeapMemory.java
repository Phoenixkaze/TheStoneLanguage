package stone.vm;

public interface HeapMemory {
    Object read(int index);
    void write(int index, Object value);
}
