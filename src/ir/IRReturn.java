package ir;

import mips.*;

public class IRReturn extends IRCommand {
  final static int BYTE_SIZE = 4;
  final static int GP_REGISTERS = 20;
  final static int MAX_SIZE = 4*GP_REGISTERS;
  final static int S_GP_REGISTERS = 9;
  final static int T_GP_REGISTERS = 10;
  private String arg;

  public IRReturn(String arg) {
    this.arg = arg;
  }

  public String toString() {
    return "return " + arg;
  }

  public void addEpilogue(MIPSGenerator g) {
      int step = 0;
      g.addCommand("; FUNCTION Epilogue:");
      g.addCommand("; Restore general-purpose registers from RAM:");
      g.addCommand("lw $ra, "+step+"($sp)");
      step += BYTE_SIZE;
      for (int i = 0; i < S_GP_REGISTERS; i++) {
          g.addCommand("lw $s"+i+", "+step+"($sp)");
          step += BYTE_SIZE;
      }
      for (int i = 0; i < T_GP_REGISTERS; i++) {
          g.addCommand("lw $t"+i+", "+step+"($sp)");
          step += BYTE_SIZE;
      }
      g.addCommand("addi $sp, $sp, 80 ; free the memory we used for spills");
  }

  public void encode(MIPSGenerator g) {
    // int argIdx = getTIdx();
    g.addCommand("add $v0, $zero, $a0");
    // addEpilogue(g);
    g.addCommand("; epilogue here -- skipping for brevity");
    g.addCommand("jr $ra");

  }
}
