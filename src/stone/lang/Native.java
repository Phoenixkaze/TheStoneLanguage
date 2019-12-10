package stone.lang;

import javax.swing.*;
import java.lang.reflect.Method;

public class Native {
    public Environment environment(Environment environment) throws StoneException {
        appendNative(environment);
        return environment;
    }

    protected void appendNative(Environment environment) throws StoneException {
        append(environment, "print", Native.class, "print", Object.class);
        append(environment, "read", Native.class, "read");
        append(environment, "length", Native.class, "length", String.class);
        append(environment, "toInt", Native.class, "toInt", Object.class);
        append(environment, "currentTime", Native.class, "currentTime");
    }

    protected void append(Environment environment, String name, Class<?> theClass, String methodName, Class<?> ... params) throws StoneException {
        Method method;
        try {
            method = theClass.getMethod(methodName, params);
        } catch (NoSuchMethodException e) {
            throw new StoneException("cannot find a native function: " + methodName);
        }
        environment.put(name, new NativeFunction(methodName, method));
    }

    public static int print(Object object) {
        System.out.println(object.toString());
        return 0;
    }

    public static String read() {
        return JOptionPane.showInputDialog(null);
    }

    public static int length(String string) {
        return string.length();
    }

    public static int toInt(Object value) {
        if (value instanceof String) {
            return Integer.parseInt((String)value);
        } else if (value instanceof Integer) {
            return ((Integer)value).intValue();
        } else {
            throw new NumberFormatException(value.toString());
        }
    }

    private static long startTime = System.currentTimeMillis();
    public static int currentTime() {
        return (int)(System.currentTimeMillis() - startTime);
    }
}
