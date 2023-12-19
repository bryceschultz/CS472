package Registers.IdEx;

import Controls.IdExControl;

public class IdExRegister {
    IdExControl control;

    public IdExControl getControl() {
        return control;
    }

    public void setControl(IdExControl control) {
        this.control = control;
    }

    @Override
    public String toString() {
        return "IdExRegister{" +
                "control=" + control +
                '}';
    }
}
