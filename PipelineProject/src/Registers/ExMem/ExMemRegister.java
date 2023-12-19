package Registers.ExMem;

import Controls.ExMemControl;

public class ExMemRegister {
    ExMemControl control;

    public ExMemControl getControl() {
        return control;
    }

    public void setControl(ExMemControl control) {
        this.control = control;
    }

    @Override
    public String toString() {
        return "ExMemRegister{" +
                "control=" + control +
                '}';
    }
}
