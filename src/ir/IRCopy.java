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
    if (!source.equals("this")) {
      if (result.matches("t[0-9]+")) {
          g.addT(result);
          int tvarIdx = g.getTIdx(result);
          if (source.matches("[0-9]+")) {
            g.addCommand("addi $t"+tvarIdx+", $zero, "+source);
          } else {
            g.addCommand("add $t"+tvarIdx+", $zero, "+source);
          }
      } else {
          if (source.matches("t[0-9]+")) {
              g.addLocalVar(result, source);
          } else if (source.matches("y")) {
              if (result.matches("[xyz]")) {
                  if (g.getLocalValue(result) == null) {
                      int next_t = g.getTSize();
                      g.addLocalVar(result,"t"+next_t);
                      g.addT("t"+next_t);
                  }
              }

              g.addLocalVar(source, g.getTAt(0));
              g.addCommand("add $"+g.getLocalValue(result)+", $zero, $"+g.getLocalValue(source));
          } else if (source.matches("[xz]")) {
            if (g.getLocalValue(result) == null) {
              int next_t = g.getTSize();
              g.addLocalVar(result,"t"+next_t);
            } 
            result = g.getLocalValue(result);
            if (g.getLocalValue(source) == null) {
              int next_t = g.getTSize();
              g.addLocalVar(source,"t"+next_t);
              g.addT("t"+next_t);
            } 
            source = g.getLocalValue(source);

            int srcIdx = g.getTIdx(source);
            if (srcIdx != -1) {
              g.addCommand("add $"+result+", $zero, $t"+srcIdx);
            } else {
            g  .addCommand("add $"+result+", $zero, $"+source);
            }
          } else {
            if (g.getLocalValue(result) == null) {
              int next_t = g.getTSize();
              g.addLocalVar(result,"t"+next_t);
            } 
            result = g.getLocalValue(result);
            int resultIdx = g.getTIdx(result);
            if (resultIdx == -1) {
              g.addCommand("addi $"+result+", $zero, "+source);
            } else {
              g.addCommand("addi $t"+resultIdx+", $zero, "+source);
            }
          }
      }
    }
  }
}
