package ir;

public class IRParam extends IRCommand {
  private String source;

  public IRParam(String source) {
    this.source = source;
  }

  public String getSource() {
    return source;
  }

  public String toString() {
    return "param " + source;
  }

  public void encode(MipsGenerator g) {
    // int paramIdx = Integer.parseInt(source.substring(1))-1;
    int paramIdx = g.getTIdx(source);
    if (paramIdx != -1) {
		g.addCommand("add $a"+g.getAIdx()+", $zero, $t"+paramIdx);
		g.incrementAIdx();
	}
  }
}
