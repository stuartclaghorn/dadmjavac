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
}
