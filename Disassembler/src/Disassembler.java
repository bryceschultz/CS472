/**
 * @author bryce schultz
 * @course CS472
 * @dueDate 10/4/2023
 */

/**
 * This module is a programming assignment for CS472 at BU.
 * <p>
 In this module we are:
 1. creating a creating and instantiating a Disassembler class
 2. Passing 11 hexadecimal instructions to the run method
 3. Determining if each individual instruction is I format or R format and what function/operation is being
 performed as well as what registers & addresses are involved.
 Supported operations include: add, sub, and, or, lw, sw, beq, bne
 * </p>
 */
public class Disassembler {
    // all supported opcodes/functions outlined as binary
    private static final int add = 0b100000;
    private static final int sub = 0b100010;
    private static final int and = 0b100100;
    private static final int or = 0b100101;
    private static final int lw = 0b100011;
    private static final int sw = 0b101011;
    private static final int beq = 0b000100;
    private static final int bne = 0b000101;
    /**
     * printFunction       (prints what function/opcode a specific instruction will be performing)
     * Input : code (int)
     * Output :
     */
    private void printFunction(int code) {
        if (code == add) System.out.print(" add");
        else if (code == sub) System.out.print(" sub");
        else if (code == and) System.out.print(" and");
        else if (code == or) System.out.print(" or");
        else if (code == lw) System.out.print(" lw");
        else if (code == sw) System.out.print(" sw");
        else if (code == beq) System.out.print(" beq");
        else if (code == bne) System.out.print(" bne");
    }

    /**
     * main       (instantiates Disassembler object/class and passes instructions & program counter to run method)
     * Input :
     * Output :
     */
    public static void main(String[] args) {
        Disassembler disassembler = new Disassembler();
        int[] instructions = {
                0x032BA020, 0x8CE90014, 0x12A90003, 0x022DA822, 0xADB30020, 0x02697824, 0xAE8FFFF4, 0x018C6020, 0x02A4A825, 0x158FFFF7, 0x8ECDFFF0
        };
        int programCounter = 0x9A040;
        disassembler.run(instructions, programCounter);
    }

    /**
     * getBits      (takes in an instruction and shifts it (shift) number spaces and applies the supplied bitMask)
     * Input : instruction (int), shift (int), bitMask(int)
     * Output : int
     * Applies the shift first then applies the bitMask which is provided as binary. Can be seen used below like this:
     * getBits(instruction, 26, 0b111111);
     * Doing the bitMask after the shift seemed more straightforward to me as you can provide all binary 1's (for as many digits as you want to extract)
     */
    private int getBits(int instruction, int shift, int bitMask) {
        return (instruction >>> shift) & bitMask;
    }

    /**
     * run     (runs the disassembler)
     * Input : instructions (int[]), programCounter (int)
     * Output :
     * Takes in an array of instructions and a programCounter to start at. This method iterates through all instructions
     * and depending on the first 6 bits (opcode) either calls the disassembleRFormat method or calls the disassembleIFormat method
     */
    private void run(int[] instructions, int programCounter) {
        for (int instruction: instructions) {
            System.out.print(String.format("%05X", programCounter));
            int opcode = getBits(instruction, 26, 0b111111);
            if (opcode == 0) {
                disassembleRFormat(instruction);
            } else {
                disassembleIFormat(instruction, opcode, programCounter);
            }
            System.out.print("\n");
            programCounter += 4;
        }
    }

    /**
     * run     (disassembles one R Format instruction)
     * Input : isntruction (int)
     * Output :
     * gets the srcRegister1 from bits 21-26, the srcRegister2 from bits 16-21, the destRegister from bits 11-16,
     * the function from bits 0-6 then prints the function and then prints the remainder of the disassembled
     * instruction (destRegister, srcRegister1, srcRegister2)
     */
    private void disassembleRFormat(int instruction) {
        int srcRegister1 = getBits(instruction, 21, 0b11111);
        int srcRegister2 = getBits(instruction, 16, 0b11111);
        int destRegister = getBits(instruction, 11, 0b11111);
        int function = getBits(instruction, 0, 0b111111);
        printFunction(function);
        System.out.print(" $" + destRegister + ", $" + srcRegister1 + ", $" + srcRegister2);
    }

    /**
     * run     (disassembles one I Format instruction)
     * Input : isntruction (int), opcode (int), programCounter (int)
     * Output :
     * gets the srcRegister from bits 21-26, the srcOrDestRegister from bits 16-21, the offset from bits 0-16 then prints
     * the opcode/function and checks if the opcode/function is bne/beq or sw/lw and prints out the disassembled
     * instruction according to the format
     */
    private void disassembleIFormat(int instruction, int opcode, int programCounter) {
        int srcRegister = getBits(instruction, 21, 0b11111);
        int srcOrDestRegister = getBits(instruction, 16, 0b11111);
        short offset = (short)getBits(instruction, 0, 0b1111111111111111);
        printFunction(opcode);
        if (opcode == bne || opcode == beq) {
            // get target address for bne or beq by adding 0x4 + pc + (offset left shifted by 2)
            int targetAddress = (programCounter + 0x4) + (offset << 2);
            System.out.print(" $" + srcRegister + ", $" + srcOrDestRegister + ", address " + String.format("%05X", targetAddress));
        }
        else if (opcode == sw || opcode == lw) System.out.print(" $" + srcOrDestRegister + ", " + offset + "($" + srcRegister + ")");
    }
}