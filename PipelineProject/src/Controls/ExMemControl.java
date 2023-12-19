package Controls;

public class ExMemControl {
    // control signals
    private int MemRead = 0;
    private int MemWrite = 0;
    private int MemToReg = 0;
    private int RegWrite = 0;

    // 3 additional data members
    private int ALUResult = 0;
    private int WriteRegNum = 0;
    private int SWValue = 0;

    public ExMemControl() {
    }

    public ExMemControl(IdExControl control) {
        if (control != null) {
            MemRead = control.getMemRead();
            MemWrite = control.getMemWrite();
            MemToReg = control.getMemToReg();
            RegWrite = control.getRegWrite();
        }
    }

    public int getMemRead() {
        return MemRead;
    }
    public int getMemWrite() {
        return MemWrite;
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
    public void setALUResult(int ALUResult) {
        this.ALUResult = ALUResult;
    }
    public int getWriteRegNum() {
        return WriteRegNum;
    }
    public void setWriteRegNum(int writeRegNum) {
        WriteRegNum = writeRegNum;
    }
    public int getSWValue() {
        return SWValue;
    }
    public void setSWValue(int SWValue) {
        this.SWValue = SWValue;
    }

    @Override
    public String toString() {
        return "ExMemControl{" +
                "MemRead=" + Integer.toBinaryString(MemRead) +
                ", MemWrite=" + Integer.toBinaryString(MemWrite) +
                ", MemToReg=" + Integer.toBinaryString(MemToReg) +
                ", RegWrite=" + Integer.toBinaryString(RegWrite) +
                ", ALUResult=" + String.format("0x%08X", ALUResult) +
                ", SWValue=" + String.format("0x%08X", SWValue) +
                ", WriteRegNum=" + WriteRegNum +
                '}';
    }
}
