package ir;

import java.util.*;
import java.io.*;
import java.nio.*;
import ir.*;

public class MipsGenerator {
  private List<String> instructions;
  private static int a_idx = 0;
  private static int t_idx = 0;
  private static int v_idx = 0;
  private Vector<String> a_s = new Vector<String>();
  private Vector<String> t_s = new Vector<String>();
  private Vector<String> v_s = new Vector<String>();
        
  public List<String> generate(List<IRCommand> irCommands) {
      instructions = new ArrayList<String>();
                  
      for (int i = 0; i < irCommands.size(); i++) {
		  irCommands.get(i).encode(this);
      }
                        
      return instructions;
  }

  public void addCommand(String s) {
      instructions.add(s);
  }

  public void incrementAIdx() {
	a_idx = a_idx + 1;
  }

  public void resetAIdx() {
	a_idx = 0;
  }

  public void incrementTIdx() {
	a_idx = a_idx + 1;
  }

  public static int getAIdx() {
    return a_idx;
  }

  public static int getTIdx() {
    return t_idx;
  }

  public int getTIdx(String t) {
    return t_s.indexOf(t);
  }

  public void addT(String t) {
    t_s.add(t);
  }

  public String toString() {
      String mips_out = new String("");
      for (String mips : instructions) {
        mips_out += mips + "\n";
      }
      return mips_out;
  }

  public boolean toMipsFile(String filename) {
    FileOutputStream fouts = null;
    OutputStreamWriter out = null;
    try {
        fouts = new FileOutputStream(filename);
        out = new OutputStreamWriter(fouts,"UTF-8");
    } catch (Exception ex) {
        System.out.println("Error: opening file - "+filename);
        return false;
    }

    String mips_out = toString();
    try {
        out.write(mips_out);
    } catch (IOException ioe) {
        System.out.println("Error: Writing Mips file");
        return false;
    } finally {
        try {
            out.close();
        } catch (Exception ex) {
            System.out.println("Error: Writing Mips file");
        }
    }
    return true;
  }
          
   /* public void visit(IRAssign command) {
      instructions.add(...);
   }
            
      public void visit(IRCall command) {
        instructions.add(...);
      } */
              
    // ...
}
