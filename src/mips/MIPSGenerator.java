package mips;

import java.util.*;
import java.io.*;
import java.nio.*;
import ir.*;

public class MIPSGenerator {
  private List<String> instructions;
  private static int a_idx = 0;
  private static int t_idx = 0;
  private static int v_idx = 0;
  private Vector<String> a_s = new Vector<String>();
  private Vector<String> t_s = new Vector<String>();
  private Vector<String> v_s = new Vector<String>();
  private Map<String,Integer> objects = new HashMap<String,Integer>();
  private Map<String,String> local_vars = new HashMap<String,String>();
  private String mipsFile;
        
  public List<String> generate(List<IRCommand> irCommands) {
      instructions = new ArrayList<String>();
                  
      for (int i = 0; i < irCommands.size(); i++) {
		  irCommands.get(i).encode(this);
      }
                        
      return instructions;
  }

  public void addLocalVar(String var, String value) {
    local_vars.put(var,value);
  }

  public String getLocalValue(String var) {
    return local_vars.get(var);
  }

  public int getLocalVarSize(String var) {
    return local_vars.size();
  }

  public String getLocalVars() {
      return local_vars.toString();
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
	t_idx = t_idx + 1;
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

  public String getTAt(int idx) {
      return t_s.get(idx);
  }

  public int getTSize() {
    return t_s.size();
  }

  public String getT() {
    return t_s.toString();
  }

  public void resetT() {
    t_s = new Vector<String>();
  }

  public void resetA() {
    a_s = new Vector<String>();
  }

  public void addObject(String name,Integer paramCount) {
      objects.put(name,paramCount);
  }

  public Integer getParamCount(String name) {
      Integer count = objects.get(name);
      if (count == null) {
        return 0;
      }
      return count;
  }

  public void setMIPSFile(String file) {
    mipsFile = file;
  }

  public String getMIPSFilename() {
      return mipsFile;
  }

  public String toString() {
      String mips_out = new String("");
      for (String mips : instructions) {
        mips_out += mips + "\n";
      }
      return mips_out;
  }

  public boolean toMIPSFile(String filename) {
    FileOutputStream fouts = null;
    OutputStreamWriter out = null;
    
    if (!mipsFile.equals(filename)) {
      mipsFile=filename;
    }
    filename = filename.substring(filename.lastIndexOf("/")+1);
    filename=filename.substring(0,filename.lastIndexOf("."))+".asm";
    System.out.println("Writing "+filename);
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
            System.out.println("Wrote "+filename);
            out.close();
        } catch (Exception ex) {
            System.out.println("Error: Writing Mips file");
        }
    }
    return true;
  }
}
