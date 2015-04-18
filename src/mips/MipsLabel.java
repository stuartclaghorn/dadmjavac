package mips;

import ir.*;

public class MipsLabel {
    public static String toString(IRLabel ir) {
      return ir.getName()+":";
    }
}
