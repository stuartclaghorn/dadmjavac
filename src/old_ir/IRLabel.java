package ir;

public class IRLabel extends IRCommand {
  private String name;

  public IRLabel(String name) {
    this.name = name;
  }

  public String toString() {
    return name + ":";
  }
}
