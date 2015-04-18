package ir;

public class IRJump extends IRCommand {
  private String label;

  public IRJump(String label) {
    this.label = label;
  }

  public String toString() {
    return "goto " + label;
  }

  public void encode(MipsGenerator g) {
    g.addCommand("j "+label);
  }
}
