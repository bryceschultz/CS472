package Controls;

public class IdExControl {
    // 7 control signals
    private int RegDst = 0;
    private int ALUSrc = 0;
    private int ALUOp = 0;
    private int MemRead = 0;
    private int MemWrite = 0;
    private int MemToReg = 0;
    private int RegWrite = 0;

    // 6 additional IdEx register data members
    private int ReadReg1Value = 0;
    private int ReadReg2Value = 0;
    private int SEOffset = 0;
    private int WriteReg_20_16 = 0;
    private int WriteReg_15_11 = 0;
    private int Function = 0;


    public int getWriteReg_20_16() {
        return WriteReg_20_16;
    }
    public void setWriteReg_20_16(int writeReg_20_16) {
        WriteReg_20_16 = writeReg_20_16;
    }
    public int getWriteReg_15_11() {
        return WriteReg_15_11;
    }
    public void setWriteReg_15_11(int writeReg_15_11) {
        WriteReg_15_11 = writeReg_15_11;
    }
    public int getFunction() {
        return Function;
    }
    public void setFunction(int function) {
        Function = function;
    }
    public int getRegDst() {
        return RegDst;
    }
    public void setRegDst(int regDst) {
        RegDst = regDst;
    }
    public int getALUSrc() {
        return ALUSrc;
    }
    public void setALUSrc(int ALUSrc) {
        this.ALUSrc = ALUSrc;
    }
    public int getALUOp() {
        return ALUOp;
    }
    public void setALUOp(int ALUOp) {
        this.ALUOp = ALUOp;
    }
    public int getMemRead() {
        return MemRead;
    }
    public void setMemRead(int memRead) {
        MemRead = memRead;
    }
    public int getMemWrite() {
        return MemWrite;
    }
    public void setMemWrite(int memWrite) {
        MemWrite = memWrite;
    }
    public int getMemToReg() {
        return MemToReg;
    }
    public void setMemToReg(int memToReg) {
        MemToReg = memToReg;
    }
    public int getRegWrite() {
        return RegWrite;
    }
    public void setRegWrite(int regWrite) {
        RegWrite = regWrite;
    }
    public int getReadReg1Value() {
        return ReadReg1Value;
    }
    public void setReadReg1Value(int readReg1Value) {
        ReadReg1Value = readReg1Value;
    }
    public int getReadReg2Value() {
        return ReadReg2Value;
    }
    public void setReadReg2Value(int readReg2Value) {
        ReadReg2Value = readReg2Value;
    }
    public int getSEOffset() {
        return SEOffset;
    }
    public void setSEOffset(int SEOffset) {
        this.SEOffset = SEOffset;
    }

    @Override
    public String toString() {
        return "IdExControl{" +
                "RegDst=" + Integer.toBinaryString(RegDst) +
                ", ALUSrc=" +  Integer.toBinaryString(ALUSrc) +
                ", ALUOp=" + Integer.toBinaryString(ALUOp) +
                ", MemRead=" + Integer.toBinaryString(MemRead) +
                ", MemWrite=" + Integer.toBinaryString(MemWrite) +
                ", MemToReg=" + Integer.toBinaryString(MemToReg) +
                ", RegWrite=" + Integer.toBinaryString(RegWrite) +
                ", ReadReg1Value=" + String.format("0x%08X", ReadReg1Value) +
                ", ReadReg2Value=" + String.format("0x%08X", ReadReg2Value) +
                ", SEOffset=" + String.format("0x%08X", SEOffset) +
                ", WriteReg_20_16=" + WriteReg_20_16 +
                ", WriteReg_15_11=" + WriteReg_15_11 +
                ", Function=" + String.format("0x%08X", Function) +
                '}';
    }
}
