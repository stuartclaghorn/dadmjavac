package mips;

import ir.*;

public class MipsParam {
    public static int a_idx = 0;

    public static String toString(IRParam ir) {
      int paramIdx = Integer.parseInt(ir.getSource().substring(1))-1;
      // increment a variable index
      // should limit to 3
      int idx = a_idx++;
      return "add $a"+idx+", $zero, $t"+paramIdx;    
    }
}
