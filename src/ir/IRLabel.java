package ir;

import mips.*;

public class IRLabel extends IRCommand {
  final static int BYTE_SIZE = 4;
  final static int GP_REGISTERS = 20;
  final static int MAX_SIZE = 4*GP_REGISTERS;
  final static int S_GP_REGISTERS = 9;
  final static int T_GP_REGISTERS = 10;
  private String name;

  public IRLabel(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return name + ":";
  }

  public void addPrologue(MIPSGenerator g) {
      int step = 0;
      g.addCommand("; FUNCTION PROLOGUE:");
      g.addCommand("; Spill all general-purpose registers into RAM:");
      g.addCommand("addi $sp, $sp, -80 ; move the stack pointer back by 80 bytes (20 gp registers * 4 bytes each");
      g.addCommand("sw $ra, "+step+"($sp)");
      step += BYTE_SIZE;
      for (int i = 0; i < S_GP_REGISTERS; i++) {
          g.addCommand("sw $s"+i+", "+step+"($sp)");
          step += BYTE_SIZE;
      }
      for (int i = 0; i < T_GP_REGISTERS; i++) {
          g.addCommand("sw $t"+i+", "+step+"($sp)");
          step += BYTE_SIZE;
      }
      g.addCommand("; FUNCTION BODY:");
  }

  public void encode(MIPSGenerator g) {
    g.addCommand(name+":");
    if (name.matches(".*_Start")) {
        // addPrologue(g);
        g.addCommand("; Prologue here -- skipping for brevity");
    }
  }
}
