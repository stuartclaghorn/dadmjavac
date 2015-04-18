package ir;

public class IRReturn extends IRCommand {
  private String arg;

  public IRReturn(String arg) {
    this.arg = arg;
  }

  public String toString() {
    return "return " + arg;
  }
}
