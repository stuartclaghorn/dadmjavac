import visitor.*;
import syntaxtree.*;
import java.util.*;

public class NameAnalysisVisitor implements Visitor {
  private JavaSymbolTable table;
  private List<String> errors;
  private List<String> definitions = new ArrayList<String>();

  public NameAnalysisVisitor(JavaSymbolTable table, List<String> errors) {
    this.table = table;
    this.errors = errors;
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
    JavaSymbolTable oldTable = table;
    table = table.getChildFor(n.i1.s, n.i1.getLine(), n.i1.getColumn());
    table = table.getChildFor("main", n.i1.getLine(), n.i1.getColumn());
    n.s.accept(this);

    table = oldTable;
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    JavaSymbolTable oldTable = table;
    table = table.getChildFor(n.i.s, n.i.getLine(), n.i.getColumn());

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
    table = table.getChildFor(n.i.s, n.i.getLine(), n.i.getColumn());

    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    }

    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.elementAt(i).accept(this);
    }

    table = oldTable;
  }

  public void visit(VarDecl n) {
    checkRedefinitions(n.i.s);
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
    JavaSymbolTable oldTable;

    checkRedefinitions(n.i.s);

    oldTable = table;
    table = table.getChildFor(n.i.s, n.i.getLine(), n.i.getColumn());

    for (int i = 0; i < n.fl.size(); i++) {
      n.fl.elementAt(i).accept(this);
    }

    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    }

    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.elementAt(i).accept(this);
    }

    table = oldTable;
  }

  // Type t;
  // Identifier i;
  public void visit(Formal n) {}

  // StatementList sl;
  public void visit(Block n) {
    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.elementAt(i).accept(this);
    }
  }

  // Exp e;
  // Statement s1,s2;
  public void visit(If n) {
    n.e.accept(this);
    n.s1.accept(this);
    n.s2.accept(this);
  }

  // Exp e;
  // Statement s;
  public void visit(While n) {
    n.e.accept(this);
    n.s.accept(this);
  }

  // Exp e;
  public void visit(Print n) {
    n.e.accept(this);
  }

  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    if (!table.contains(n.i.s)) {
      errors.add(String.format("Use of undefined identifier %s at line %d, character %d", n.i.s, n.getLine(), n.getColumn()));
    }

    n.i.accept(this);
    n.e.accept(this);
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    if (!table.contains(n.i.s)) {
      errors.add(String.format("Use of undefined identifier %s at line %d, character %d", n.i.s, n.getLine(), n.getColumn()));
    }

    n.i.accept(this);
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(And n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(LessThan n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(Plus n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(Minus n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(Times n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e1,e2;
  public void visit(ArrayLookup n) {
    n.e1.accept(this);
    n.e2.accept(this);
  }

  // Exp e;
  public void visit(ArrayLength n) {
    n.e.accept(this);
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public void visit(Call n) {
    n.e.accept(this);
    n.i.accept(this);
    for ( int i = 0; i < n.el.size(); i++ ) {
      n.el.elementAt(i).accept(this);
    }
  }

  // String s;
  public void visit(IdentifierExp n) {
    if (!table.contains(n.s)) {
      errors.add(String.format("Use of undefined identifier %s at line %d, character %d", n.s, n.getLine(), n.getColumn()));
    }
  }

  // Identifier i;
  public void visit(NewObject n) {
    if (!table.contains(n.i.s)) {
      errors.add(String.format("Use of undefined identifier %s at line %d, character %d", n.i.s, n.getLine(), n.getColumn()));
    }
  }

  // String s;
  public void visit(Identifier n) {}

  // These AST nodes do not rely on the symbol table, so they don't need to be
  // visited. Nonetheless, we're required to have visiting actions for them.
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

  private void checkRedefinitions(String symbolName) {
    if (table.getSymbols().containsKey(symbolName) && !definitions.contains(symbolName)) {
      List<JavaSymbolTable.Symbol> symbols = table.getSymbols().get(symbolName);

      if (symbols.size() > 1) {
        for (int i = 1; i < symbols.size(); i++) {
          JavaSymbolTable.Symbol symbol = symbols.get(i);

          errors.add(String.format("Multiply defined identifier %s at line %d, character %d", symbolName, symbol.getLine(), symbol.getColumn()));
        }
      }

      definitions.add(symbolName);
    }
  }
}
