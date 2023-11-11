
/**
 * Class: CacheSlot
 * Description: this class is used to organize a specified slot in the cache and is made up of the number (slot number),
 * validBit, dirtyBit, tag, blockBeginAddress, and data.
 */
public class CacheSlot {
    private int number = 0;
    private int validBit = 0;
    private int dirtyBit = 0;
    private int tag = 0;
    private int blockBeginAddress = 0;
    private int[] data = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    // default constructor
    CacheSlot() {
    }

    // getters and setters
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }

    public int getValidBit() { return validBit; }
    public void setValidBit(int validBit) { this.validBit = validBit; }

    public int getDirtyBit() { return dirtyBit; }
    public void setDirtyBit(int dirtyBit) { this.dirtyBit = dirtyBit; }

    public int getTag() { return tag; }
    public void setTag(int tag) { this.tag = tag; }

    public int[] getData() { return data; }
    public void setData(int[] data) { this.data = data; }

    public int getBlockBeginAddress() { return blockBeginAddress; }
    public void setBlockBeginAddress(int blockBeginAddress) { this.blockBeginAddress = blockBeginAddress; }
}
