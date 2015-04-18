package ir;

public class IRParam extends IRCommand {
  private String source;

  public IRParam(String source) {
    this.source = source;
  }

  public String toString() {
    return "param " + source;
  }
}
