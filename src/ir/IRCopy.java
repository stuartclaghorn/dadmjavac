package ir;

public class IRCopy extends IRCommand {
  private String result, source;

  public IRCopy(String result, String source) {
    this.result = result;
    this.source = source;
  }

  public String getResult() {
    return result;
  }

  public String getSource() {
    return source;
  }

  public String toString() {
    return result + " := " + source;
  }

  public void encode(MipsGenerator g) {
    // int tvarIdx = Integer.parseInt(result.substring(1))-1;
    g.addT(result);
    int tvarIdx = g.getTIdx(result);
    g.addCommand("addi $t"+tvarIdx+", $zero, "+source);
  }
}
