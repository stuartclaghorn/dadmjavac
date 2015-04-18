import java.util.*;
import visitor.*;
import syntaxtree.*;
import ir.*;

public class IRGenerationVisitor implements TypeVisitor {
  private JavaSymbolTable table;
  private List<IRCommand> commands;
  private int temporaryNumber = 0;
  private Stack<String> temporaries = new Stack<String>();
  private int labelNumber = 0;

  public IRGenerationVisitor(JavaSymbolTable table) {
    this.table = table;
  }

  public List<IRCommand> generateIR(Program n) {
    commands = new ArrayList<IRCommand>();
    visit(n);
    return commands;
  }

  // MainClass m;
  // ClassDeclList cl;
  public Type visit(Program n) {
    n.m.accept(this);
    for (int i = 0; i < n.cl.size(); i++) {
      n.cl.elementAt(i).accept(this);
    }

    return null;
  }

  // Identifier i1, i2;
  // Statement s;
  public Type visit(MainClass n) {
    JavaSymbolTable oldTable = table;
    table = table.getChildFor(n.i1.s, n.i1.getLine(), n.i1.getColumn());
    table = table.getChildFor("main", n.i1.getLine(), n.i1.getColumn());

    emitMethodLabel(n.i1.s, "main");
    n.s.accept(this);

    commands.add(new IRCall(pushTemporary(), "_system_exit", 0));

    table = oldTable;

    return null;
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public Type visit(ClassDeclSimple n) {
    JavaSymbolTable oldTable = table;
    table = table.getChildFor(n.i.s, n.i.getLine(), n.i.getColumn());

    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    }

    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.elementAt(i).accept(this);
    }

    table = oldTable;

    return null;
  }

  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public Type visit(ClassDeclExtends n) {
    JavaSymbolTable oldTable = table;
    table = table.getChildFor(n.i.s, n.i.getLine(), n.i.getColumn());

    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    }

    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.elementAt(i).accept(this);
    }

    table = oldTable;

    return null;
  }

  public Type visit(VarDecl n) {
    return null;
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public Type visit(MethodDecl n) {
    JavaSymbolTable oldTable;

    oldTable = table;
    table = table.getChildFor(n.i.s, n.i.getLine(), n.i.getColumn());

    emitMethodLabel(oldTable.getParent().getName(), table.getParent().getName());

    for (int i = 0; i < n.fl.size(); i++) {
      n.fl.elementAt(i).accept(this);
    }

    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    }

    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.elementAt(i).accept(this);
    }

    commands.add(new IRReturn(variableOrTemporary(n.e)));

    table = oldTable;

    return null;
  }

  // Type t;
  // Identifier i;
  public Type visit(Formal n) {
    return null;
  }

  // StatementList sl;
  public Type visit(Block n) {
    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.elementAt(i).accept(this);
    }

    return null;
  }

  // Exp e;
  // Statement s1,s2;
  public Type visit(If n) {
    String elseLabel = generateLabel();
    String continueLabel = generateLabel();

    commands.add(new IRConditionalJump(variableOrTemporary(n.e), elseLabel));

    n.s1.accept(this);
    commands.add(new IRJump(continueLabel));
    commands.add(new IRLabel(elseLabel));
    n.s2.accept(this);
    commands.add(new IRLabel(continueLabel));

    return null;
  }

  // Exp e;
  // Statement s;
  public Type visit(While n) {
    String enterLabel = generateLabel();
    String exitLabel = generateLabel();

    commands.add(new IRLabel(enterLabel));
    commands.add(new IRConditionalJump(variableOrTemporary(n.e), exitLabel));

    n.s.accept(this);
    commands.add(new IRJump(enterLabel));
    commands.add(new IRLabel(exitLabel));

    return null;
  }

  // Exp e;
  public Type visit(Print n) {
    commands.add(new IRParam(variableOrTemporary(n.e)));
    commands.add(new IRCall(pushTemporary(), "_system_out_println", 1));

    return null;
  }

  // Identifier i;
  // Exp e;
  public Type visit(Assign n) {
    String leftTemporary, rightTemporary;

    if (n.e instanceof And) {
      leftTemporary = variableOrTemporary(((And)n.e).e1);
      rightTemporary = variableOrTemporary(((And)n.e).e2);

      commands.add(new IRAssign(n.i.s, "&&", leftTemporary, rightTemporary));
    }
    else if (n.e instanceof LessThan) {
      leftTemporary = variableOrTemporary(((LessThan)n.e).e1);
      rightTemporary = variableOrTemporary(((LessThan)n.e).e2);

      commands.add(new IRAssign(n.i.s, "<", leftTemporary, rightTemporary));
    }
    else if (n.e instanceof Plus) {
      leftTemporary = variableOrTemporary(((Plus)n.e).e1);
      rightTemporary = variableOrTemporary(((Plus)n.e).e2);

      commands.add(new IRAssign(n.i.s, "+", leftTemporary, rightTemporary));
    }
    else if (n.e instanceof Minus) {
      leftTemporary = variableOrTemporary(((Minus)n.e).e1);
      rightTemporary = variableOrTemporary(((Minus)n.e).e2);

      commands.add(new IRAssign(n.i.s, "-", leftTemporary, rightTemporary));
    }
    else if (n.e instanceof Times) {
      leftTemporary = variableOrTemporary(((Times)n.e).e1);
      rightTemporary = variableOrTemporary(((Times)n.e).e2);

      commands.add(new IRAssign(n.i.s, "*", leftTemporary, rightTemporary));
    }
    else if (n.e instanceof IntegerLiteral) {
      commands.add(new IRCopy(n.i.s, "" + ((IntegerLiteral)n.e).i));
    }
    else if (n.e instanceof True) {
      commands.add(new IRCopy(n.i.s, 1 + ""));
    }
    else if (n.e instanceof False) {
      commands.add(new IRCopy(n.i.s, 0 + ""));
    }
    else if (n.e instanceof This) {
      commands.add(new IRCopy(n.i.s, "this"));
    }
    else if (n.e instanceof NewArray) {
      commands.add(new IRNewArray(n.i.s, "int[]", variableOrTemporary(((NewArray)n.e).e)));
    }
    else if (n.e instanceof NewObject) {
      commands.add(new IRNewObject(n.i.s, ((NewObject)n.e).i.s));
    }
    else {
      commands.add(new IRCopy(n.i.s, variableOrTemporary(n.e)));
    }

    return null;
  }

  // Identifier i;
  // Exp e1,e2;
  public Type visit(ArrayAssign n) {
    n.i.accept(this);
    n.e1.accept(this);
    n.e2.accept(this);

    return null;
  }

  // Exp e1,e2;
  public Type visit(And n) {
    String leftTemporary = variableOrTemporary(n.e1);
    String rightTemporary = variableOrTemporary(n.e2);

    commands.add(new IRAssign(pushTemporary(), "&&", leftTemporary, rightTemporary));

    return null;
  }

  // Exp e1,e2;
  public Type visit(LessThan n) {
    String leftTemporary = variableOrTemporary(n.e1);
    String rightTemporary = variableOrTemporary(n.e2);

    commands.add(new IRAssign(pushTemporary(), "<", leftTemporary, rightTemporary));

    return null;
  }

  // Exp e1,e2;
  public Type visit(Plus n) {
    String leftTemporary = variableOrTemporary(n.e1);
    String rightTemporary = variableOrTemporary(n.e2);

    commands.add(new IRAssign(pushTemporary(), "+", leftTemporary, rightTemporary));

    return null;
  }

  // Exp e1,e2;
  public Type visit(Minus n) {
    String leftTemporary = variableOrTemporary(n.e1);
    String rightTemporary = variableOrTemporary(n.e2);

    commands.add(new IRAssign(pushTemporary(), "-", leftTemporary, rightTemporary));

    return null;
  }

  // Exp e1,e2;
  public Type visit(Times n) {
    String leftTemporary = variableOrTemporary(n.e1);
    String rightTemporary = variableOrTemporary(n.e2);

    commands.add(new IRAssign(pushTemporary(), "*", leftTemporary, rightTemporary));

    return null;
  }

  // Exp e1,e2;
  public Type visit(ArrayLookup n) {
    String leftTemporary = variableOrTemporary(n.e1);
    String rightTemporary = variableOrTemporary(n.e2);

    commands.add(new IRIndexedRead(pushTemporary(), leftTemporary, rightTemporary));

    return null;
  }

  // Exp e;
  public Type visit(ArrayLength n) {
    commands.add(new IRLength(pushTemporary(), variableOrTemporary(n.e)));

    return null;
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public Type visit(Call n) {
    ArrayList<String> temporaryParams = new ArrayList<String>();
    IdentifierType leftType = (IdentifierType)n.e.accept(this);
    String label;

    n.i.accept(this);

    for (int i = 0; i < n.el.size(); i++) {
      n.el.elementAt(i).accept(this);
    }

    for (int i = n.el.size(); i >= 0; i--) {
      temporaryParams.add(0, popTemporary());
    }

    for (String temporaryParam : temporaryParams) {
      commands.add(new IRParam(temporaryParam));
    }

    label = leftType.s + "_" + n.i.s;
    commands.add(new IRCall(pushTemporary(), label, n.el.size() + 1));

    return null;
  }

  // String s;
  public Type visit(IdentifierExp n) {
    commands.add(new IRCopy(pushTemporary(), n.s));

    return null;
  }

  // Identifier i;
  public Type visit(NewObject n) {
    commands.add(new IRNewObject(pushTemporary(), n.i.s));

    return new IdentifierType(n.i.s);
  }

  // String s;
  public Type visit(Identifier n) {
    return null;
  }

  public Type visit(IntegerLiteral n) {
    commands.add(new IRCopy(pushTemporary(), n.i + ""));

    return null;
  }

  public Type visit(True n) {
    commands.add(new IRCopy(pushTemporary(), 1 + ""));

    return null;
  }

  public Type visit(False n) {
    commands.add(new IRCopy(pushTemporary(), 0 + ""));

    return null;
  }

  public Type visit(IntArrayType n) {
    return null;
  }
  public Type visit(BooleanType n) {
    return null;
  }
  public Type visit(IntegerType n) {
    return null;
  }
  public Type visit(IdentifierType n) {
    return null;
  }

  public Type visit(This n) {
    commands.add(new IRCopy(pushTemporary(), "this"));

    return new IdentifierType(table.getParent().getTable().getParent().getName());
  }

  public Type visit(NewArray n) {
    n.e.accept(this);
    commands.add(new IRNewArray(pushTemporary(), "int[]", popTemporary()));

    return null;
  }

  public Type visit(Not n) {
    commands.add(new IRAssign(pushTemporary(), "!", variableOrTemporary(n.e)));

    return null;
  }


  private void emitMethodLabel(String className, String methodName) {
    commands.add(new IRLabel(className + "_" + methodName));
  }

  private String generateLabel() {
    return "L" + (++labelNumber);
  }

  private String pushTemporary() {
    String temporary = "t" + (++temporaryNumber);

    temporaries.push(temporary);

    return temporary;
  }

  private String popTemporary() {
    return temporaries.pop();
  }

  private String variableOrTemporary(Exp expression) {
    String result;

    if (expression instanceof IdentifierExp) {
      result = ((IdentifierExp)expression).s;
    }
    else {
      expression.accept(this);
      result = popTemporary();
    }

    return result;
  }
}
