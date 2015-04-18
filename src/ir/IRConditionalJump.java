package ir;

import mips.*;

public class IRConditionalJump extends IRCommand {
  private String arg, label;

  public IRConditionalJump(String arg, String label) {
    this.arg = arg;
    this.label = label;
  }

  public String toString() {
    return "iffalse " + arg + " goto " + label;
  }

  public void encode(MIPSGenerator g) {
    int argIdx = g.getTIdx(arg);
    g.addCommand("beq $t"+argIdx+", $zero, "+label);
  }
}
