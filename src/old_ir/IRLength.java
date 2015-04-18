package ir;

public class IRLength extends IRCommand {
  private String result, arg;

  public IRLength(String result, String arg) {
    this.result = result;
    this.arg = arg;
  }

  public String toString() {
    return result + " := " + arg;
  }
}
