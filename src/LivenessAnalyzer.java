import java.util.*;
import ir.*;

public class LivenessAnalyzer {
  private List<IRCommand> commands;
  private List<IRCommand> successors[];
  private List<String> uses[];
  private List<String> defs[];
  private List<String> ins[];
  private List<String> outs[];

  public LivenessAnalyzer(List<IRCommand> commands) {
    this.commands = commands;
    this.successors = new ArrayList[commands.size()];
    this.uses = new ArrayList[commands.size()];
    this.defs = new ArrayList[commands.size()];
    this.ins = new ArrayList[commands.size()];
    this.outs = new ArrayList[commands.size()];
  }

  public void analyze() {
    for (int index = 0; index < commands.size(); index++) {
      IRCommand command = commands.get(index);

      this.successors[index] = identifySuccessors(command);
      this.uses[index] = identifyUses(command);
      this.defs[index] = identifyDefs(command);
      this.ins[index] = new ArrayList<String>();
      this.outs[index] = new ArrayList<String>();
    }
  }

  private List<String> identifySuccessors(IRCommand command) {
    
  }
}
