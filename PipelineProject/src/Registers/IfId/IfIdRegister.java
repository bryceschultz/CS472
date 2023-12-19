package Registers.IfId;

public class IfIdRegister {
    private Integer value;

    public Integer getValue() { return value; }
    public void setValue(Integer value) { this.value = value; }

    @Override
    public String toString() {
        if (getValue() == null) {
            return "CONTROL=000000000";
        } else {
            return String.format("0x%08X", getValue());
        }
    }
}
