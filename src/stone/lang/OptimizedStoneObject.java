package stone.lang;

public class OptimizedStoneObject {
    public static class AccessException extends Exception {}
    protected OptimizedClassInfo classInfo;
    protected Object[] fields;

    public OptimizedStoneObject(OptimizedClassInfo classInfo, int size) {
        this.classInfo = classInfo;
        fields = new Object[size];
    }

    public OptimizedClassInfo classInfo() {
        return classInfo;
    }

    public Object read(String name) throws AccessException {
        Integer i = classInfo.fieldIndex(name);
        if (i != null) {
            return fields[i];
        } else {
            i = classInfo.methodIndex(name);
            if (i != null) {
                return method(i);
            }
        }
        throw new AccessException();
    }

    public Object read(int index) {
        return fields[index];
    }

    public void write(int index, Object value) {
        fields[index] = value;
    }

    public void write(String name, Object value) throws AccessException {
        Integer i = classInfo.fieldIndex(name);
        if (i == null)
            throw new AccessException();
        else
            fields[i] = value;
    }

    public Object method(int index) {
        return classInfo.method(this, index);
    }
}
