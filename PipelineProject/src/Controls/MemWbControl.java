package Controls;

public class MemWbControl {
    // control signals
    private int MemToReg = 0;
    private int RegWrite = 0;

    // 3 additional data members
    private int ALUResult = 0;
    private int WriteRegNum = 0;
    private int LWDataValue = 0;

    public MemWbControl() {}

    public MemWbControl(ExMemControl control) {
        if (control != null) {
            MemToReg = control.getMemToReg();
            RegWrite = control.getRegWrite();
            ALUResult = control.getALUResult();
            WriteRegNum = control.getWriteRegNum();
        }
    }

    public int getMemToReg() {
        return MemToReg;
    }
    public int getRegWrite() {
        return RegWrite;
    }
    public int getALUResult() {
        return ALUResult;
    }
    public int getWriteRegNum() {
        return WriteRegNum;
    }
    public int getLWDataValue() {
        return LWDataValue;
    }
    public void setLWDataValue(int LWDataValue) {
        this.LWDataValue = LWDataValue;
    }

    @Override
    public String toString() {
        return "MemWbControl{" +
                "MemToReg=" + Integer.toBinaryString(MemToReg) +
                ", RegWrite=" + Integer.toBinaryString(RegWrite) +
                ", LWDataValue=" + String.format("0x%08X", LWDataValue) +
                ", ALUResult=" + String.format("0x%08X", ALUResult) +
                ", WriteRegNum=" + WriteRegNum +
                '}';
    }
}
