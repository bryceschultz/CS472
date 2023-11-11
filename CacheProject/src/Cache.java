import java.util.Arrays;

/**
 * Class: Cache
 * Description: this class is used to organize the cache and is made up of numSlots & offsetSize which are constants/final
 * and the cache (which is 16 CacheSlot objects), the mainMemory which is an array of ints 0x7FF in length,
 * and the largestValueAssignableToByte which is 0xFF and is used to help build out the mainMemory array.
 */
public class Cache {
    private final int numSlots = 0x10;
    private final int offsetSize = 0x10;
    private CacheSlot[] cache = new CacheSlot[numSlots];
    private int[] mainMemory = new int[0x7FF];
    private int largestValueAssignableToByte = 0xFF;

    // Constructor: builds out starting point for cache and mainMemory
    Cache() {
        for (int i = 0; i < cache.length; i++) {
            cache[i] = new CacheSlot();
            cache[i].setNumber(i);
        }
        int currentAssignment = 0;
        for (int i = 0; i < mainMemory.length; i++) {
            if (currentAssignment <= largestValueAssignableToByte) {
                mainMemory[i] = currentAssignment;
                currentAssignment += 1;
            } else {
                mainMemory[i] = 0;
                currentAssignment = 1;
            }
        }
    }

    /**
     * display      (prints out the cache)
     * Input : currentInstruction (String)
     * Output : none
     * This method takes in the currentInstruction as a string and then prints out the cache
     * including each slot as well as the Slot number, Valid bit, Tag, Dirty bit, and associated data
     * for each slot.
     */
    public void display(String currentInstruction) {
        System.out.println(currentInstruction);
        System.out.println("Slot\tValid\tTag\tDirty\tData");
        for (int i = 0; i < cache.length; i++) {
            String tableRow = String.format("%01X", cache[i].getNumber()) + "\t" +
                    String.format("%01X", cache[i].getValidBit()) + "\t" +
                    String.format("%01X", cache[i].getTag()) + "\t" +
                    String.format("%01X", cache[i].getDirtyBit()) + "\t";
            for (int dataValue: cache[i].getData()) {
                tableRow += String.format("%01X", dataValue) + "\t";
            }
            System.out.println(tableRow);
        }

    }

    /**
     * write      (write to the cache)
     * Input : currentInstruction (String), address (int), data (int)
     * Output : none
     * This method takes in the currentInstruction (String), address (int), dataAsStr (int)
     * and prints them out then calls the updateCache method with address and data as input
     */
    public void write(String currentInstruction, int address, int data) {
        System.out.println(currentInstruction);
        System.out.println("What address would you like to write to?");
        System.out.println(String.format("%01X", address));
        System.out.println("What data would you like to write to that address?");
        System.out.println(String.format("%01X", data));
        updateCache(address, true, data);
    }

    /**
     * read     (read from the cache)
     * Input : currentInstruction (String), addressAsStr (String)
     * Output : none
     * This method takes in the currentInstruction (String), addressAsStr (int)
     * and prints them out then calls the updateCache method with address as input
     */
    public void read(String currentInstruction, int address) {
        System.out.println(currentInstruction);
        System.out.println("What address would you like to read?");
        System.out.println(String.format("%01X", address));
        updateCache(address, false, 0);
    }

    /**
     * updateCache      (read from the cache)
     * Input : address (int), write (boolean), dataToWrite (int)
     * Output : none
     * This method takes in the address and dataToWrite (if any) and the boolean input 'write' which determines
     * if this update is writing or just reading (reads and writes will produce different print messages from the method
     * and determine if the writeToCache method is called).
     * This method starts by getting the offset, slotNum, blockBeginAddress, blockEndAddress, and tag. Once these
     * values are extracted the method goes through the cache and finds the slot number for this specific update that
     * matches the slot number from the cache.
     * If the matching slot numbers are accompanied by matching tags and a valid bit of 1 then we have a cache hit.
     * In the case of a cache hit we just print out either the read or write requested and call out the Hit.
     * If this is a cache miss then we check if there's a dirty bit that we will be overwriting, if there is a dirty bit then
     * we take the slot and write it to memory before moving on. The next step in the cache miss operation is to copy
     * the data from the mainMemory starting at blockBeginAddress and ending at blockEndAddress to our cache, and setting the
     * valid bit to 1, and setting the specified tag, and setting the blockBeginAddress for the slot, and finally
     * printing out the read or write and calling out the Miss.
     * After the cache hit or cache miss actions we check if this update includes a write, if it's a write then we call the
     * writeToCache method with dataToWrite, slotNum, and offset as input.
     */
    private void updateCache(int address, boolean write, int dataToWrite) {
        int offset = address & 0x0F;
        int slotNum = (address & 0xF0) >>> 4;
        int blockBeginAddress = address & 0xFF0;
        int blockEndAddress = blockBeginAddress + offsetSize;
        int tag = address >>> 8;
        for (CacheSlot slot: cache) {
            if (slotNum == slot.getNumber()) {
                int tagFromCache = slot.getTag();
                if (tag == tagFromCache && slot.getValidBit() == 1) {
                    if (write) System.out.println(String.format("%01X", dataToWrite) + " has been written to address " + String.format("%01X", address) + " (Cache Hit) ");
                    else System.out.println("At that byte there is the value " + String.format("%01X", slot.getData()[offset]) + " (Cache Hit) ");
                } else {
                    if (slot.getDirtyBit() == 1) {
                        writeToMemory(slotNum, slot.getBlockBeginAddress());
                        slot.setDirtyBit(0);
                    }
                    int[] dataFromMemory = Arrays.copyOfRange(mainMemory, blockBeginAddress, blockEndAddress);
                    slot.setData(dataFromMemory);
                    slot.setValidBit(1);
                    slot.setTag(tag);
                    slot.setBlockBeginAddress(blockBeginAddress);
                    if (write) System.out.println(String.format("%01X", dataToWrite) + " has been written to address " + String.format("%01X", address) + " (Cache Miss) ");
                    else System.out.println("At that byte there is the value " +  String.format("%01X", slot.getData()[offset]) + " (Cache Miss) ");
                }
                if (write) writeToCache(dataToWrite, slotNum, offset);
                return;
            }
        }
    }

    /**
     * writeToCache      (write to the cache)
     * Input : dataToWrite (int), slotNum (int), offset (int)
     * Output : none
     * This method takes in the dataToWrite, slotNum, and offset and starts by creating a clone of the
     * specified slot in the cache, then sets the dirty bit for this slot to 1 then updates the cloned slot
     * with the new data provided and finally finishes by replacing the original slot data with the new updated
     * data created via the clone operation.
     */
    private void writeToCache(int dataToWrite, int slotNum, int offset) {
        int[] updatedData = cache[slotNum].getData().clone();
        cache[slotNum].setDirtyBit(1);
        updatedData[offset] = dataToWrite;
        cache[slotNum].setData(updatedData);
    }

    /**
     * writeToMemory     (write to the memory)
     * Input : slotNum (int), blockBeginAddress (int)
     * Output : none
     * This method takes in the slotNum and the blockBeginAddress and goes through the main memory array and replaces
     * the data in the mainMemory with the corresponding data from the cache. This method is called from the
     * updateCache method when there is going to be a read or write to a slot with a dirty bit == 1. This method
     * allows data to persist to memory so that it will not be overwritten by other data entering the cache.
     */
    private void writeToMemory(int slotNum, int blockBeginAddress) {
        int offset = 0;
        for (int i = 0; i < mainMemory.length; i++) {
            if (i == blockBeginAddress) {
                for (CacheSlot slot: cache) {
                    if (slot.getNumber() == slotNum) {
                        for (int j = 0; j < offsetSize; j++) {
                            mainMemory[i] = slot.getData()[offset];
                            offset++;
                            i++;
                        }
                        return;
                    }
                }
            }
        }
    }
}
