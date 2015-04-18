import visitor.*;
import syntaxtree.*;
import java.util.*;

public class SymbolTableGenerationVisitor implements Visitor {
  private JavaSymbolTable table;

  public SymbolTableGenerationVisitor(JavaSymbolTable table) {
    this.table = table;
  }

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    n.m.accept(this);
    for (int i = 0; i < n.cl.size(); i++) {
      n.cl.elementAt(i).accept(this);
    }
  }

  // Identifier i1, i2;
  // Statement s;
  public void visit(MainClass n) {
    JavaSymbolTable child = table.addWithChild(new JavaSymbolTable.ClassSymbol(n.i1.toString(), n.i1.getLine(), n.i1.getColumn()));
    JavaSymbolTable.MethodSymbol mainMethodSymbol = new JavaSymbolTable.MethodSymbol("main", null, n.i1.getLine(), n.i1.getColumn());
    JavaSymbolTable grandchild, oldTable;

    mainMethodSymbol.setIsStatic(true);
    grandchild = child.addWithChild(mainMethodSymbol);

    oldTable = table;
    table = grandchild;

    grandchild.add(new JavaSymbolTable.VariableSymbol(n.i2.toString(), null, n.i2.getLine(), n.i2.getColumn()));
    n.s.accept(this);

    table = oldTable;
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    JavaSymbolTable oldTable = table;
    table = table.addWithChild(new JavaSymbolTable.ClassSymbol(n.i.toString(), n.i.getLine(), n.i.getColumn()));

    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    }

    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.elementAt(i).accept(this);
    }

    table = oldTable;
  }

  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    JavaSymbolTable oldTable = table;
    table = table.addWithChild(new JavaSymbolTable.ClassSymbol(n.i.toString(), n.i.getLine(), n.i.getColumn()));

    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    }

    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.elementAt(i).accept(this);
    }

    table = oldTable;
  }

  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
    table.add(new JavaSymbolTable.VariableSymbol(n.i.toString(), n.t, n.i.getLine(), n.i.getColumn()));
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
    JavaSymbolTable oldTable = table;
    table = table.addWithChild(new JavaSymbolTable.MethodSymbol(n.i.toString(), n.t, n.i.getLine(), n.i.getColumn()));

    for (int i = 0; i < n.fl.size(); i++) {
      n.fl.elementAt(i).accept(this);
    }

    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    }

    table = oldTable;
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {
    table.add(new JavaSymbolTable.VariableSymbol(n.i.toString(), n.t, n.i.getLine(), n.i.getColumn()));

    if (table.getParent() != null) {
      ((JavaSymbolTable.MethodSymbol)table.getParent()).addArgument(n.t);
    }
  }

  // These AST nodes do not impact the symbol table, so they don't need to be
  // visited. Nonetheless, we're required to have visiting actions for them.
  public void visit(Block n) {}
  public void visit(If n) {}
  public void visit(While n) {}
  public void visit(Print n) {}
  public void visit(Assign n) {}
  public void visit(ArrayAssign n) {}
  public void visit(And n) {}
  public void visit(LessThan n) {}
  public void visit(Plus n) {}
  public void visit(Minus n) {}
  public void visit(Times n) {}
  public void visit(ArrayLookup n) {}
  public void visit(ArrayLength n) {}
  public void visit(Call n) {}
  public void visit(IdentifierExp n) {}
  public void visit(NewObject n) {}
  public void visit(Identifier n) {}
  public void visit(IntArrayType n) {}
  public void visit(BooleanType n) {}
  public void visit(IntegerType n) {}
  public void visit(IdentifierType n) {}
  public void visit(IntegerLiteral n) {}
  public void visit(True n) {}
  public void visit(False n) {}
  public void visit(This n) {}
  public void visit(NewArray n) {}
  public void visit(Not n) {}
}
