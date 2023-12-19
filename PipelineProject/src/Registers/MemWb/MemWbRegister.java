package Registers.MemWb;

import Controls.MemWbControl;

public class MemWbRegister {
    MemWbControl control;

    public MemWbControl getControl() {
        return control;
    }

    public void setControl(MemWbControl control) {
        this.control = control;
    }

    @Override
    public String toString() {
        return "MemWbRegister{" +
                "control=" + control +
                '}';
    }
}
