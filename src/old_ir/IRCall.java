package ir;

public class IRCall extends IRCommand {
  private String result, label;
  private int parametersCount;

  public IRCall(String result, String label, int parametersCount) {
    this.result = result;
    this.label = label;
    this.parametersCount = parametersCount;
  }

  public String toString() {
    return result + " := call "  + label + ", " + parametersCount;
  }
}
