package ir;

public class IRCopy extends IRCommand {
  private String result, source;

  public IRCopy(String result, String source) {
    this.result = result;
    this.source = source;
  }

  public String toString() {
    return result + " := " + source;
  }
}
