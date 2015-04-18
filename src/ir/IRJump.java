package ir;

import mips.*;

public class IRJump extends IRCommand {
  private String label;

  public IRJump(String label) {
    this.label = label;
  }

  public String toString() {
    return "goto " + label;
  }

  public void encode(MIPSGenerator g) {
    g.addCommand("j "+label);
  }
}
