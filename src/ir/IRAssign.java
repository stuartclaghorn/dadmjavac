package ir;

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

  public void encode(MipsGenerator g) {
    g.addT(result);
    int tvarIdx = g.getTIdx(result);
    int arg1Idx = g.getTIdx(arg1);
    int arg2Idx = g.getTIdx(arg2);
    switch (op) {
        case "+":
            g.addCommand("add $t"+tvarIdx+", $t"+arg1Idx+", $t"+arg2Idx);
            break;
        case "*":
            g.addCommand("mult $t"+tvarIdx+", $t"+arg1Idx+", $t"+arg2Idx);
            break;
    }
  }
}
