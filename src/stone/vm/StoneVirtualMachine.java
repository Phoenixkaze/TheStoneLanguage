package stone.vm;

import stone.lang.NativeFunction;
import stone.lang.StoneException;
import stone.lang.ast.AbstractSyntaxTreeList;

import java.util.ArrayList;

import static stone.vm.OperationCode.*;

public class StoneVirtualMachine {
    protected byte[] code;
    protected Object[] stack;
    protected String[] strings;
    protected HeapMemory heap;
    protected Object[] registers;

    public int pc, fp, sp, ret;
    public final static int NUMOFREG = 6;
    public final static int SAVEAREASIZE = NUMOFREG + 2;
    public final static int TRUE = 1;
    public final static int FALSE = 0;

    public StoneVirtualMachine(int codeSize, int stackSize, int stringsSize, HeapMemory heapMemory) {
        code = new byte[codeSize];
        stack = new Object[stackSize];
        strings = new String[stringsSize];
        registers = new Object[NUMOFREG];
        heap = heapMemory;
    }

    public Object getRegister(int i) {
        return registers[i];
    }

    public void setRegister(int i, Object value) {
        registers[i] = value;
    }

    public String[] strings() {
        return strings;
    }

    public byte[] code() {
        return code;
    }

    public Object[] stack() {
        return stack;
    }

    public HeapMemory heap() {
        return heap;
    }

    public void run(int entry) throws StoneException {
        pc = entry;
        fp = 0;
        sp = 0;
        ret = -1;
        while (pc >= 0) {
            mainLoop();
        }
    }

    protected void mainLoop() throws StoneException {
        switch (code[pc]) {
            case ICONST:
                registers[decodeRegister(code[pc + 5])] = readInt(code, pc + 1);
                pc += 6;
                break;
            case BCONST:
                registers[decodeRegister(code[pc + 2])] = (int)code[pc + 1];
                pc += 3;
                break;
            case SCONST:
                registers[decodeRegister(code[pc + 3])] = strings[readShort(code, pc + 1)];
                pc += 4;
                break;
            case MOVE:
                moveValue();
                break;
            case GMOVE:
                moveHeapValue();
                break;
            case IFZERO: {
                Object value = registers[decodeRegister(code[pc + 1])];
                if (value instanceof Integer && ((Integer)value).intValue() == 0) {
                    pc += readShort(code, pc + 2);
                } else {
                    pc += 4;
                }
                break;
            }
            case GOTO:
                pc += readShort(code, pc + 1);
                break;
            case CALL:
                callFunction();
                break;
            case RETURN:
                pc = ret;
                break;
            case SAVE:
                saveRegisters();
                break;
            case RESTORE:
                restoreRegisters();
                break;
            case NEG: {
                int reg = decodeRegister(code[pc + 1]);
                Object value = registers[reg];
                if (value instanceof Integer) {
                    registers[reg] = -((Integer)value).intValue();
                } else {
                    throw new StoneException("bad operand value");
                }
                pc += 2;
                break;
            }
            default:
                if (code[pc] > LESS) {
                    throw new StoneException("bad instruction");
                } else {
                    computeNumber();
                }
                break;
        }
    }

    protected void computeNumber() throws StoneException {
        int left = decodeRegister(code[pc + 1]);
        int right = decodeRegister(code[pc + 2]);
        Object v1 = registers[left];
        Object v2 = registers[right];
        boolean areNumbers = v1 instanceof Integer && v2 instanceof Integer;
        if (code[pc] == ADD && !areNumbers) {
            if (v1 == null) {
                registers[left] = v2 == null ? TRUE : FALSE;
            } else {
                registers[left] = v1.equals(v2) ? TRUE : FALSE;
            }
        } else {
            if (!areNumbers) {
                throw new StoneException("invalid operand value");
            }
            int i1 = ((Integer)v1).intValue();
            int i2 = ((Integer)v2).intValue();
            int i3;
            switch (code[pc]) {
                case ADD:
                    i3 = i1 + i2;
                    break;
                case SUB:
                    i3 = i1 - i2;
                    break;
                case MUL:
                    i3 = i1 * i2;
                    break;
                case DIV:
                    i3 = i1 % i2;
                    break;
                case EQUAL:
                    i3 = i1 == i2 ? TRUE : FALSE;
                    break;
                case MORE:
                    i3 = i1 > i2 ? TRUE : FALSE;
                    break;
                case LESS:
                    i3 = i1 < i2 ? TRUE : FALSE;
                    break;
                default:
                    throw new StoneException("never reach here");
            }
            registers[left] = i3;
        }
        pc += 3;
    }

    protected void restoreRegisters() {
        int destination = decodeOffset(code[pc + 1]) + fp;
        for (int i = 0; i < NUMOFREG; i++) {
            registers[i] = stack[destination++];
        }
        sp = fp;
        fp = ((Integer)stack[destination++]).intValue();
        ret = ((Integer)stack[destination++]).intValue();
        pc += 2;
    }

    protected void saveRegisters() {
        int size = decodeOffset(code[pc + 1]);
        int destination = size + sp;
        for (int i = 0; i < NUMOFREG; i++) {
            stack[destination++] = registers[i];
        }
        stack[destination++] = fp;
        fp = sp;
        sp += size + SAVEAREASIZE;
        stack[destination++] = ret;
        pc += 2;
    }

    protected void callFunction() throws StoneException {
        Object value = registers[decodeRegister(code[pc + 1])];
        int numberOfArgs = code[pc + 2];
        if (value instanceof VirtualMachineFunction
                && ((VirtualMachineFunction)value).parameters().size() == numberOfArgs) {
            ret = pc + 3;
            pc = ((VirtualMachineFunction)value).entry();
        } else if (value instanceof NativeFunction && ((NativeFunction)value).getNumberOfParameters() == numberOfArgs) {
            Object[] args = new Object[numberOfArgs];
            for (int i = 0; i < numberOfArgs; i++) {
                args[i] = stack[sp + i];
            }
            stack[sp] = ((NativeFunction)value).invoke(args, new AbstractSyntaxTreeList(new ArrayList<>()));
            pc += 3;
        } else {
            throw new StoneException("invalid function call");
        }
    }

    protected void moveHeapValue() {
        byte rand = code[pc + 1];
        if (isRegister(rand)) {
            int destination = readShort(code, pc + 2);
            heap.write(destination, registers[decodeRegister(rand)]);
        } else {
            int source = readShort(code, pc + 2);
            registers[decodeRegister(code[pc + 3])] = heap.read(source);
        }
        pc += 4;
    }

    protected void moveValue() {
        byte source = code[pc + 1];
        byte destination = code[pc + 2];
        Object value;
        if (isRegister(source)) {
            value = registers[decodeRegister(source)];
        } else {
            value = stack[fp + decodeOffset(source)];
        }
        if (isRegister(destination)) {
            registers[decodeRegister(destination)] = value;
        } else {
            stack[fp + decodeOffset(destination)] = value;
        }
        pc += 3;
    }

    public static int readInt(byte[] array, int index) {
        return (array[index] << 24) | ((array[index + 1] & 0xff) << 16) | ((array[index + 2] & 0xff) << 8) | (array[index + 3] & 0xff);
    }

    public static int readShort(byte[] array, int index) {
        return (array[index] << 8) | (array[index + 1] & 0xff);
    }
}
