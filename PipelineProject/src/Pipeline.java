import Registers.IdEx.*;
import Registers.IfId.*;
import Registers.ExMem.*;
import Registers.MemWb.*;
import Controls.ExMemControl;
import Controls.IdExControl;
import Controls.MemWbControl;

public class Pipeline {
    private final int[] Main_Mem = new int[0x7FF];
    private final int[] Regs = new int[32];
    private int Program_Counter = 0;
    private static final int add = 0x20;
    private static final int sub = 0x22;
    private static final int lb = 0x20;
    private static final int sb = 0x28;

    private final int[] Instruction_Cache = {
            0xa1020000,
            0x810AFFFC,
            0x00831820,
            0x01263820,
            0x01224820,
            0x81180000,
            0x81510010,
            0x00624022,
            0x00000000, // This is a nop, used just to allow the "real" instructions finish in the pipeline
            0x00000000, // This is a nop, used just to allow the "real" instructions finish in the pipeline
            0x00000000, // This is a nop, used just to allow the "real" instructions finish in the pipeline
            0x00000000 // This is a nop, used just to allow the "real" instructions finish in the pipeline
    };

    // Constructor: builds out starting point for cache and mainMemory
    Pipeline() {
        int currentAssignment = 0;
        for (int i = 0; i < Main_Mem.length; i++) {
            int largestValueAssignableToByte = 0xFF;
            if (currentAssignment <= largestValueAssignableToByte) {
                Main_Mem[i] = currentAssignment;
                currentAssignment += 1;
            } else {
                Main_Mem[i] = 0;
                currentAssignment = 1;
            }
        }
        for (int i = 0; i < Regs.length; i++) {
            if (i == 0) Regs[i] = 0;
            else Regs[i] = (0x100 + i);
        }
    }

    public void run() {
        // create all pipeline registers and set no ops in each register
        IfIdRead ifIdRead = new IfIdRead();
        ifIdRead.setValue(0x0);
        IfIdWrite ifIdWrite = new IfIdWrite();
        ifIdWrite.setValue(0x0);

        IdExRead idExRead = new IdExRead();
        idExRead.setControl(new IdExControl());
        IdExWrite idExWrite = new IdExWrite();
        idExWrite.setControl(new IdExControl());

        ExMemRead exMemRead = new ExMemRead();
        exMemRead.setControl(new ExMemControl());
        ExMemWrite exMemWrite = new ExMemWrite();
        exMemWrite.setControl(new ExMemControl());

        MemWbRead memWbRead = new MemWbRead();
        memWbRead.setControl(new MemWbControl());
        MemWbWrite memWbWrite = new MemWbWrite();
        memWbWrite.setControl(new MemWbControl());

        // run through each instruction in the Instruction_Cache
        for (int i = 0; i < Instruction_Cache.length; i++) {
            runOneCycle(i + 1, ifIdRead, ifIdWrite, idExRead, idExWrite,
                    exMemRead, exMemWrite, memWbRead, memWbWrite);
        }
    };

    public void runOneCycle(int clockCycle, IfIdRead ifIdRead, IfIdWrite ifIdWrite,
                            IdExRead idExRead, IdExWrite idExWrite,
                            ExMemRead exMemRead, ExMemWrite exMemWrite,
                            MemWbRead memWbRead, MemWbWrite memWbWrite) {
        IF_stage(ifIdWrite);
        ID_stage(ifIdRead, idExWrite);
        EX_stage(idExRead, exMemWrite);
        MEM_stage(exMemRead, memWbWrite);
        WB_stage(memWbRead);

        Print_out_everything(clockCycle, ifIdRead, ifIdWrite, idExRead, idExWrite,
                exMemRead, exMemWrite, memWbRead, memWbWrite);
        Copy_write_to_read(ifIdRead, ifIdWrite, idExRead, idExWrite,
                exMemRead, exMemWrite, memWbRead, memWbWrite);
    };

    private int getBits(int instruction, int shift, int bitMask) {
        return (instruction >>> shift) & bitMask;
    }

    private void IF_stage(IfIdWrite ifIdWrite) {
        // Write to Write version of ifId register
        ifIdWrite.setValue(Instruction_Cache[Program_Counter]);
        Program_Counter++;
    };

    private void ID_stage(IfIdRead ifIdRead, IdExWrite idExWrite) {
        // Read from Read version of ifId register
        Integer instruction = ifIdRead.getValue();
        if (instruction != null) {
            IdExControl control = new IdExControl();
            // extract opcode and set function
            int opcode = getBits(instruction, 26, 0b111111);
            control.setFunction(getBits(instruction, 0, 0b111111));

            // set ReadReg1Value, ReadReg2Value, WriteReg_15_11, WriteReg_20_16, and SEOffset
            control.setReadReg1Value(Regs[getBits(instruction, 21, 0b11111)]);
            control.setReadReg2Value(Regs[getBits(instruction, 16, 0b11111)]);
            control.setWriteReg_15_11(getBits(instruction, 11, 0b11111));
            control.setWriteReg_20_16(getBits(instruction, 16, 0b11111));
            control.setSEOffset((short) getBits(instruction, 0, 0b1111111111111111));

            if (instruction == 0x0) {
                // this instruction is a no op
                control.setRegWrite(0);
                control.setMemWrite(0);
            } else if (opcode == 0b0) {
                // set control signals for r format
                control.setRegDst(0b1);
                control.setALUSrc(0b0);
                control.setALUOp(0b10);
                control.setMemRead(0b0);
                control.setMemWrite(0b0);
                control.setMemToReg(0b0);
                control.setRegWrite(0b1);
            } else if (opcode == lb) {
                // lb as indicated by opcode (i format)
                control.setRegDst(0b0);
                control.setALUSrc(0b1);
                control.setALUOp(0b0);
                control.setMemRead(0b1);
                control.setMemWrite(0b0);
                control.setMemToReg(0b1);
                control.setRegWrite(0b1);
            }  else if (opcode == sb) {
                // sb as indicated by opcode (i format)
                // set RegDst and MemToReg to 0, even though we don't care about them
                control.setALUSrc(0b1);
                control.setALUOp(0b0);
                control.setRegDst(0b0);
                control.setMemRead(0b0);
                control.setMemWrite(0b1);
                control.setMemToReg(0b0);
                control.setRegWrite(0b0);
            }
            // Write to Write version of idEx register
            idExWrite.setControl(control);
        }
    };

    public void EX_stage(IdExRead idExRead, ExMemWrite exMemWrite) {
        if (idExRead.getControl() != null) {
            // Read from Read version of idEx register
            IdExControl idExControl = idExRead.getControl();
            // pass along MemRead, MemWrite, MemToReg, RegWrite by passing idExRead.getControl
            // to the ExMemControl constructor
            ExMemControl exMemControl = new ExMemControl(idExRead.getControl());

            exMemControl.setSWValue(idExControl.getReadReg2Value());
            int lowerALUInput;
            if (idExControl.getALUSrc() == 0) {
                lowerALUInput = idExControl.getReadReg2Value();
            } else {
                lowerALUInput = idExControl.getSEOffset();
            }
            if (idExControl.getALUOp() == 0b00) {
                // this is a load or store, the alu operation will be add by default
                exMemControl.setALUResult(idExControl.getReadReg1Value() + lowerALUInput);
            } else if (idExControl.getALUOp() == 0b10) {
                // this is r format, we need to get function code
                if (idExControl.getFunction() == add) {
                    // add
                    exMemControl.setALUResult(idExControl.getReadReg1Value() + lowerALUInput);
                } else if (idExControl.getFunction() == sub) {
                    // subtract
                    exMemControl.setALUResult(idExControl.getReadReg1Value() - lowerALUInput);
                }
            }

            if (idExControl.getRegDst() == 0) {
                exMemControl.setWriteRegNum(idExControl.getWriteReg_20_16());
            } else {
                exMemControl.setWriteRegNum(idExControl.getWriteReg_15_11());
            }

            // Write to Write version of exMem register
            exMemWrite.setControl(exMemControl);
        }
    };

    public void MEM_stage(ExMemRead exMemRead, MemWbWrite memWbWrite) {
        if (exMemRead.getControl() != null) {
            // Read from Read version of exMem register
            ExMemControl exMemControl = exMemRead.getControl();
            // pass along MemToReg, RegWrite, ALUResult, WriteRegNum by passing exMemRead.getControl
            // to the MemWbControl constructor
            MemWbControl memWbControl = new MemWbControl(exMemRead.getControl());

            if  (exMemControl.getMemRead() == 1) {
                memWbControl.setLWDataValue(Main_Mem[exMemControl.getALUResult()]);
            }

            if (exMemControl.getMemWrite() == 1) {
                // It writes the value of least significant byte in the register specified.
                Main_Mem[exMemControl.getALUResult()] = (exMemControl.getSWValue() & 0xFF);
            }
            // Write to Write version of memWb register
            memWbWrite.setControl(memWbControl);
        }
    };

    public void WB_stage(MemWbRead memWbRead) {
        if (memWbRead.getControl() != null) {
            // Read from Read version of memWb register
            MemWbControl memWbControl = memWbRead.getControl();

            if (memWbControl.getRegWrite() == 1) {
                if (memWbControl.getMemToReg() == 0) {
                    Regs[memWbControl.getWriteRegNum()] = memWbControl.getALUResult();
                } else {
                    // Loads the value of least significant byte and sets it in the Regs
                    Regs[memWbControl.getWriteRegNum()] = (memWbControl.getLWDataValue() & 0xFF);
                }
            }
        }
    };

    public void Print_out_everything(int clockCycle,
                                     IfIdRead ifIdRead, IfIdWrite ifIdWrite,
                                     IdExRead idExRead, IdExWrite idExWrite,
                                     ExMemRead exMemRead, ExMemWrite exMemWrite,
                                     MemWbRead memWbRead, MemWbWrite memWbWrite) {
        System.out.print("\n\n\nCLOCK CYCLE: " + clockCycle);
        System.out.print("\nREGS: \n");
        for (int i = 0; i < Regs.length; i++) {
            System.out.print("$" + i + "=" + String.format("0x%03X", Regs[i]) + "\t");
            if (i % 6 == 5) System.out.println();
        }
        System.out.println("\n\nIfId_WRITE: " + ifIdWrite);
        System.out.println("IfId_READ: " + ifIdRead);

        System.out.println("\nIdEx_WRITE: " + idExWrite);
        System.out.println("IdEx_READ: " + idExRead);

        System.out.println("\nExMem_WRITE: " + exMemWrite);
        System.out.println("ExMem_READ: " + exMemRead);

        System.out.println("\nMemWb_WRITE: " + memWbWrite);
        System.out.println("MemWb_READ: " + memWbRead);
    };

    public void Copy_write_to_read(IfIdRead ifIdRead, IfIdWrite ifIdWrite,
                                   IdExRead idExRead, IdExWrite idExWrite,
                                   ExMemRead exMemRead, ExMemWrite exMemWrite,
                                   MemWbRead memWbRead, MemWbWrite memWbWrite) {
        ifIdRead.setValue(ifIdWrite.getValue());
        idExRead.setControl(idExWrite.getControl());
        exMemRead.setControl(exMemWrite.getControl());
        memWbRead.setControl(memWbWrite.getControl());
    };
}
