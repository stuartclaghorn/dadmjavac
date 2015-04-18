package ir;

public class IRNewObject extends IRCommand {
  private String result, type;

  public IRNewObject(String result, String type) {
    this.result = result;
    this.type = type;
  }

  public String toString() {
    return result + " := new " + type;
  }
}
