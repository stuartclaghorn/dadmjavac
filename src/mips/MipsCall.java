package mips;

import ir.*;

public class MipsCall {
    public static String toString(IRCall ir) {
        return "jal "+ir.getLabel();
    }
}
