package ir;

public class IRIndexedRead extends IRCommand {
  private String result, arg, index;

  public IRIndexedRead(String result, String arg, String index) {
    this.result = result;
    this.arg = arg;
    this.index = index;
  }

  public String toString() {
    return result + " := " + arg + "[" + index + "]";
  }
}
