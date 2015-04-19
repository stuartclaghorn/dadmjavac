package ir;

import mips.*;

public class IRAssign extends IRCommand {
  private String result, op, arg1, arg2;

  public IRAssign(String result, String op, String arg1, String arg2) {
    this.result = result;
    this.op = op;
    this.arg1 = arg1;
    this.arg2 = arg2;
  }

  public IRAssign(String result, String op, String arg1) {
    this.result = result;
    this.op = op;
    this.arg1 = arg1;
  }

  public String getResult() {
    return result;
  }

  public String getOp() {
    return op;
  }

  public String getArg1() {
    return arg1;
  }

  public String getArg2() {
    return arg2;
  }

  public String toString() {
    String string = result + " := ";

    if (arg2 != null) {
      string += arg1 + " " + op + " " + arg2;
    }
    else {
      string += op + " " + arg1;
    }

    return string;
  }

  public void encode(MIPSGenerator g) {
    // look for y
    if (arg1.equals("y")) {
        arg1 = "t0";
    }
    if (arg2.equals("y")) {
        arg2 = "t0";
    }

    // look for x
    if (arg1.equals("x")) {
      arg1 = g.getLocalValue(arg1);
    }

    if (arg2.equals("x")) {
      arg2 = g.getLocalValue(arg2);
    }

    if (result.equals("x")) {
        result = g.getLocalValue(result);
    }

    g.addT(result);
    // System.out.println("Result: "+result);

    // System.out.println("op: "+op);
    // System.out.println("arg1: "+arg1);
    // System.out.println("arg2: "+arg2);
    int tvarIdx = g.getTIdx(result);
    int arg1Idx = g.getTIdx(arg1);
    int arg2Idx = g.getTIdx(arg2);
    // Check for tvarIdx arg1Idx arg2Idx != -1
    switch (op) {
        case "+":
            g.addCommand("add $t"+tvarIdx+", $t"+arg1Idx+", $t"+arg2Idx);
            break;
        case "*":
            g.addCommand("mult $t"+tvarIdx+", $t"+arg1Idx+", $t"+arg2Idx);
            break;
        case "<":
            if (arg1Idx != -1)  {
              g.addCommand("slt $t"+tvarIdx+", $t"+arg1Idx+", $t"+arg2Idx);
            } else {
              g.addCommand("slt $t"+tvarIdx+", $a0, $t"+arg2Idx);
            }
            break;
        default:
            g.addCommand("Unknown IRAssign op - "+op);
            break;
    }
  }
}
