package ir;

import mips.*;

public class IRCopy extends IRCommand {
  private String result, source;

  public IRCopy(String result, String source) {
    this.result = result;
    this.source = source;
  }

  public String getResult() {
    return result;
  }

  public String getSource() {
    return source;
  }

  public String toString() {
    return result + " := " + source;
  }

  public void encode(MIPSGenerator g) {
    // int tvarIdx = Integer.parseInt(result.substring(1))-1;
    if (!source.equals("this")) {
      if (!result.equals("x")) {
          g.addT(result);
          int tvarIdx = g.getTIdx(result);
          g.addCommand("addi $t"+tvarIdx+", $zero, "+source);
      } else {
          if (source.matches("t[0-9]+")) {
              g.addLocalVar(result, source);
          } else if (source.matches("y")) {
              source = "t0";
              g.addLocalVar(result, source);
              result = "t1";
              g.addT(result);
              g.addCommand("addi $"+result+", $zero, $"+source);
          } else {
              result = g.getLocalValue(result);
              int resultIdx = g.getTIdx(result);
              g.addCommand("addi $t"+resultIdx+", $zero, "+source);
          }
      }
    }
  }
}
