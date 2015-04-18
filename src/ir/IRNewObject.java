package ir;

import mips.*;

public class IRNewObject extends IRCommand {
  private String result, type;

  public IRNewObject(String result, String type) {
    this.result = result;
    this.type = type;
  }
  public String toString() {
    return result + " := new " + type;
  }

  public void encode(MIPSGenerator g) {
    // g.addCommand(this.getClass().getSimpleName()+" Mips");
  }
}
