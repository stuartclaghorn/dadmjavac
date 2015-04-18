package mips;

import ir.*;

// ir.getOp? - need to use operation to determine first command?
public class MipsAssign {
    public static String toString(IRAssign ir) {
      int tvarIdx = Integer.parseInt(ir.getResult().substring(1)) - 1;
      int arg1Idx = Integer.parseInt(ir.getArg1().substring(1)) - 1;
      int arg2Idx = Integer.parseInt(ir.getArg2().substring(1)) - 1;

      // hard-coded add
      return "add $t"+tvarIdx+", $t"+arg1Idx+", $t"+arg2Idx;
    }
}
