package ir;

import mips.*;

public class IRCall extends IRCommand {
  private String result, label;
  private int parametersCount;

  public IRCall(String result, String label, int parametersCount) {
    this.result = result;
    this.label = label;
    this.parametersCount = parametersCount;
  }

  public String getLabel() {
    return label;
  }

  public String toString() {
    return result + " := call "  + label + ", " + parametersCount;
  }

  public void encode(MIPSGenerator g) {
    g.addCommand("jal " + label);

    if (!label.matches("_.*")) {
      g.addT(result);
      int ResultIdx = g.getTIdx(result);
      g.addCommand("add $t"+ResultIdx+", $zero, $v0");
    }
    g.addObject(label,parametersCount);

    g.resetAIdx();
  }
}
