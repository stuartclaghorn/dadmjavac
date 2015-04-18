package ir;

import mips.*;

public class IRNewArray extends IRCommand {
  private String result, type, size;

  public IRNewArray(String result, String type, String size) {
    this.result = result;
    this.type = type;
    this.size = size;
  }

  public String toString() {
    return result + " := new " + type + ", " + size;
  }

  public void encode(MIPSGenerator g) {
    g.addCommand(this.getClass().getSimpleName()+" Mips");
  }
}
