import java_cup.runtime.*;
import java.io.*;
import java.util.*;
import syntaxtree.*;
import visitor.*;
import ir.*;
import mips.*;

public class JavaCompiler {
  private String filepath;
  private String irs_out = new String("");

  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("ERROR: Invalid number of arguments (expected 1, got " + args.length + ")");
      System.err.println("Usage: java JavaCompiler HelloWorld.java");
      System.exit(1);
    }

    try {
      new JavaCompiler(args[0]).call();
    }
    catch (FileNotFoundException e) {
      System.err.println("ERROR: No such file " + args[0]);
      System.exit(1);
    }
  }

  public JavaCompiler(String filepath) {
    this.filepath = filepath;
  }

  public void call() throws FileNotFoundException {
    Symbol tree = null;
    FileInputStream input = new FileInputStream(filepath);
    Reader reader = new InputStreamReader(input);
    JavaLexer lexer = new JavaLexer(reader);
    JavaParser parser = new JavaParser(lexer);
    List<String> errors = new ArrayList<String>();

    try {
      tree = parser.parse();
      errors = parser.getErrors();
    }
    catch (Exception e) {
      System.err.println(String.format("ERROR: Fatal parse error at line %d, column %d", parser.getLine(), parser.getColumn()));
    }

    if (errors.size() == 0) {
      if (tree != null) {
        Program program = (Program)tree.value;
        JavaSymbolTable table = new JavaSymbolTable();

        // Build the program's symbol table.
        Visitor symbolTableGenerationVisitor = new SymbolTableGenerationVisitor(table);
        symbolTableGenerationVisitor.visit(program);

        // Check the program for multiple definitions of the same symbol in a
        // single scope and uses of undefined symbols. Append errors to the
        // running list of errors.
        Visitor nameAnalysisVisitor = new NameAnalysisVisitor(table, errors);
        nameAnalysisVisitor.visit(program);

        // Check the program for mismatched types. Append errors to the running
        // list of errors.
        TypeVisitor typeAnalysisVisitor = new TypeAnalysisVisitor(table, errors);
        typeAnalysisVisitor.visit(program);

        if (errors.size() == 0) {
          IRGenerationVisitor irGenerationVisitor = new IRGenerationVisitor(table);
          List<IRCommand> intermediateRepresentation = irGenerationVisitor.generateIR(program);
          MIPSGenerator mipsGenerator = new MIPSGenerator();

          System.out.println("IR:");
          for (IRCommand command : intermediateRepresentation) {
            System.out.println(command.toString());
            irs_out += command.toString();
          }
          System.out.println();

          System.out.println("MIPS:");
          mipsGenerator.setMIPSFile(filepath);
          mipsGenerator.generate(intermediateRepresentation);
          System.out.print(mipsGenerator.toString());
          if (!mipsGenerator.toMIPSFile(filepath)) {
            System.err.println("ERROR: writing MIPS file");
          }

        }
        else {
          for (String error : errors) {
            System.err.println("ERROR: " + error);
          }
        }
      }
    }
    else {
      for (String error : parser.getErrors()) {
        System.err.println("ERROR: " + error);
      }
    }
  }
}
