package mips;

import ir.*;

public class MipsCopy {
    public static String toString(IRCopy ir) {
      int tvarIdx = Integer.parseInt(ir.getResult().substring(1))-1;
      return "addi $t"+tvarIdx+", $zero, "+ir.getSource();
    }
}
