package stone.vm;

public class Code {
    protected StoneVirtualMachine virtualMachine;
    protected int codeSize;
    protected int numberOfStrings;
    protected int nextRegister;
    protected int frameSize;

    public StoneVirtualMachine getVirtualMachine() {
        return virtualMachine;
    }

    public void setVirtualMachine(StoneVirtualMachine virtualMachine) {
        this.virtualMachine = virtualMachine;
    }

    public int getCodeSize() {
        return codeSize;
    }

    public void setCodeSize(int codeSize) {
        this.codeSize = codeSize;
    }

    public int getNumberOfStrings() {
        return numberOfStrings;
    }

    public void setNumberOfStrings(int numberOfStrings) {
        this.numberOfStrings = numberOfStrings;
    }

    public int getNextRegister() {
        return nextRegister;
    }

    public void setNextRegister(int nextRegister) {
        this.nextRegister = nextRegister;
    }

    public int moveToNextRegister() {
        return nextRegister++;
    }

    public int getFrameSize() {
        return frameSize;
    }

    public void setFrameSize(int frameSize) {
        this.frameSize = frameSize;
    }

    public Code(StoneVirtualMachine virtualMachine) {
        this.virtualMachine = virtualMachine;
        codeSize = 0;
        numberOfStrings = 0;
    }

    public int position() {
        return codeSize;
    }

    public void set(short value, int position) {
        virtualMachine.code()[position] = (byte)(value >> 8);
        virtualMachine.code()[position + 1] = (byte)value;
    }

    public void add(byte b) {
        virtualMachine.code()[codeSize++] = b;
    }

    public void add(short i) {
        add((byte)(i >>> 8));
        add((byte)i);
    }

    public void add(int i) {
        add((byte)(i >>> 24));
        add((byte)(i >>> 16));
        add((byte)(i >>> 8));
        add((byte)i);
    }

    public int record(String string) {
        virtualMachine.strings()[numberOfStrings] = string;
        return numberOfStrings++;
    }
}
