import java.util.Arrays;
import java.util.List;

/**
 * Class: TestCache
 * Description: this class is used to test the other classes within this module.
 * The test goes as follows:
 * 1. create a new Cache object
 * 2. loop through the instructions array and dispatch to the appropriate method
 *      a. if the next instruction is 'D' display the cache
 *      b. if the next instruction is 'W' write to the cache with the next two
 *      items from the instruction array as input to the write method
 *      c. if the next instruction is 'R' read from the cache with the next one
 *      item from the instruction array as input to the read method
 * 3. continue step 2 until there are no more instructions within the instructions array.
 */
public class TestCache {
    public static void main(String[] args) {
        TestCache testCache = new TestCache();
        testCache.runTest();
    }

    /**
     * runTest      (runs cache test for module)
     * Input : none
     * Output : none
     * This method is used to test the other classes within this module, specifically as outlined above.
     */
    private void runTest() {
        Cache cache = new Cache();
        System.out.println("(R)ead, (W)rite, or (D)isplay Cache?");
        for (int i = 0; i < instructions.size(); i++) {
            String currentInstruction = (String)instructions.get(i);
            switch (currentInstruction) {
                case "D":
                    cache.display(currentInstruction);
                    System.out.println("(R)ead, (W)rite, or (D)isplay Cache?");
                    break;
                case "W":
                    cache.write(currentInstruction, (int)instructions.get(i+1), (int)instructions.get(i + 2));
                    System.out.println("(R)ead, (W)rite, or (D)isplay Cache?");
                    i += 2;
                    break;
                case "R":
                    cache.read(currentInstruction, (int)instructions.get(i+1));
                    System.out.println("(R)ead, (W)rite, or (D)isplay Cache?");
                    i += 1;
                    break;
            }
        }
    }

    private List<Object> instructions = Arrays.asList(
            "R", 0x5, "R", 0x6, "R", 0x7, "R", 0x14c, "R", 0x14d, "R", 0x14e, "R",
            0x14f, "R", 0x150, "R", 0x151, "R", 0x3A6, "R", 0x4C3, "D", "W", 0x14c,
            0x99, "W", 0x63B, 0x7, "R", 0x582, "D", "R", 0x348, "R", 0x3F, "D", "R",
            0x14b, "R", 0x14c, "R", 0x63F, "R", 0x83, "D"
    );
}
