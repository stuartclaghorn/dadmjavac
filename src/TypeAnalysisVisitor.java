import visitor.*;
import syntaxtree.*;
import java.util.*;

public class TypeAnalysisVisitor implements TypeVisitor {
  JavaSymbolTable table;
  List<String> errors;

  public TypeAnalysisVisitor(JavaSymbolTable table, List<String> errors) {
    this.table = table;
    this.errors = errors;
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

  // Identifier i1,i2;
  // Statement s;
  public Type visit(MainClass n) {
    JavaSymbolTable oldTable = table;
    table = table.getChildFor(n.i1.s, n.i1.getLine(), n.i1.getColumn());
    table = table.getChildFor("main", n.i1.getLine(), n.i1.getColumn());

    n.i1.accept(this);
    n.i2.accept(this);
    n.s.accept(this);

    table = oldTable;

    return null;
  }

  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public Type visit(ClassDeclSimple n) {
    JavaSymbolTable oldTable = table;
    table = table.getChildFor(n.i.s, n.i.getLine(), n.i.getColumn());

    n.i.accept(this);
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

    n.i.accept(this);
    n.j.accept(this);
    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    }
    for (int i = 0; i < n.ml.size(); i++) {
      n.ml.elementAt(i).accept(this);
    }

    table = oldTable;

    return null;
  }

  // Type t;
  // Identifier i;
  public Type visit(VarDecl n) {
    n.t.accept(this);
    n.i.accept(this);

    return null;
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public Type visit(MethodDecl n) {
    JavaSymbolTable oldTable = table;
    table = table.getChildFor(n.i.s, n.i.getLine(), n.i.getColumn());

    n.t.accept(this);
    n.i.accept(this);
    for (int i = 0; i < n.fl.size(); i++) {
      n.fl.elementAt(i).accept(this);
    }
    for (int i = 0; i < n.vl.size(); i++) {
      n.vl.elementAt(i).accept(this);
    }
    for (int i = 0; i < n.sl.size(); i++) {
      n.sl.elementAt(i).accept(this);
    }
    n.e.accept(this);

    table = oldTable;

    return null;
  }

  // Type t;
  // Identifier i;
  public Type visit(Formal n) {
    n.t.accept(this);
    n.i.accept(this);

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

  // String s;
  public Type visit(IdentifierType n) {
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
    if (!(n.e.accept(this) instanceof BooleanType)) {
      errors.add(String.format("Non-boolean expression used as the condition of an if statement at line %d, character %d", 7, 7));
    }

    n.s1.accept(this);
    n.s2.accept(this);

    return null;
  }

  // Exp e;
  // Statement s;
  public Type visit(While n) {
    Type type = n.e.accept(this);

    if (!(n.e.accept(this) instanceof BooleanType)) {
      errors.add(String.format("Non-boolean expression used as the condition of a while statement at line %d, character %d", 7, 7));
    }

    n.s.accept(this);

    return null;
  }

  // Exp e;
  public Type visit(Print n) {
    if (!(n.e.accept(this) instanceof IntegerType)) {
      errors.add(String.format("Call to method println does not match its declared signature at line %d, character %d", 7, 7));
    }

    return null;
  }

  // Identifier i;
  // Exp e;
  public Type visit(Assign n) {
    JavaSymbolTable.Symbol left, right;
    Type rightType = n.e.accept(this);

    if ((left = table.get(n.i.s)) != null) {
      if (left.isWritable()) {
        Type leftType = ((JavaSymbolTable.VariableSymbol)left).getType();

        if (rightType != null && !(rightType.getClass().equals(leftType.getClass()))) {
          errors.add(String.format("Type mismatch during assignment at line %d, character %d", 7, 7));
        }
      }
      else {
        errors.add(String.format("Invalid l-value (%s is a %s) at line %d, character %d", left.getName(), left.category(), left.getLine(), left.getColumn()));
      }
    }

    if (rightType instanceof IdentifierType
      && !(n.e instanceof NewObject)
      && (right = table.get(((IdentifierType)rightType).s)) != null
      && !right.isReadable())
    {
      errors.add(String.format("Invalid r-value (%s is a %s) at line %d, character %d", right.getName(), right.category(), right.getLine(), right.getColumn()));
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
    Type leftType = n.e1.accept(this);
    Type rightType = n.e2.accept(this);

    checkOperandReadability("&&", leftType, rightType);

    if (!(leftType instanceof BooleanType) || !(rightType instanceof BooleanType)) {
      errors.add(String.format("Attempt to use boolean operator && on non-boolean operands at line %d, character %d", 7, 7));
    }

    return new BooleanType();
  }

  // Exp e1,e2;
  public Type visit(LessThan n) {
    Type leftType = n.e1.accept(this);
    Type rightType = n.e2.accept(this);

    checkOperandReadability("<", leftType, rightType);

    if (!(leftType instanceof IntegerType)) {
      errors.add(String.format("Non-integer operand for operator < at line %d, character %d", 7, 7));
    }
    if (!(rightType instanceof IntegerType)) {
      errors.add(String.format("Non-integer operand for operator < at line %d, character %d", 7, 7));
    }

    return new BooleanType();
  }

  // Exp e1,e2;
  public Type visit(Plus n) {
    Type leftType = n.e1.accept(this);
    Type rightType = n.e2.accept(this);

    checkOperandReadability("+", leftType, rightType);

    if (!(leftType instanceof IntegerType)) {
      errors.add(String.format("Non-integer operand for operator + at line %d, character %d", 7, 7));
    }
    if (!(rightType instanceof IntegerType)) {
      errors.add(String.format("Non-integer operand for operator + at line %d, character %d", 7, 7));
    }

    return new IntegerType();
  }

  // Exp e1,e2;
  public Type visit(Minus n) {
    Type leftType = n.e1.accept(this);
    Type rightType = n.e2.accept(this);

    if (!(leftType instanceof IntegerType)) {
      errors.add(String.format("Non-integer operand for operator - at line %d, character %d", 7, 7));
    }
    if (!(rightType instanceof IntegerType)) {
      errors.add(String.format("Non-integer operand for operator - at line %d, character %d", 7, 7));
    }

    return new IntegerType();
  }

  // Exp e1,e2;
  public Type visit(Times n) {
    Type leftType = n.e1.accept(this);
    Type rightType = n.e2.accept(this);

    checkOperandReadability("*", leftType, rightType);

    if (!(leftType instanceof IntegerType)) {
      errors.add(String.format("Non-integer operand for operator * at line %d, character %d", 7, 7));
    }
    if (!(rightType instanceof IntegerType)) {
      errors.add(String.format("Non-integer operand for operator * at line %d, character %d", 7, 7));
    }

    return new IntegerType();
  }

  // Exp e1,e2;
  public Type visit(ArrayLookup n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return new IntegerType();
  }

  // Exp e;
  public Type visit(ArrayLength n) {
    if (!(n.e.accept(this) instanceof IntArrayType)) {
      errors.add(String.format("Length property only applies to arrays at line %d, character %d", 7, 7));
    }

    return new IntegerType();
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public Type visit(Call n) {
    Type receiverType = n.e.accept(this);
    JavaSymbolTable.Symbol symbol = null;
    Type result = null;

    if (receiverType instanceof IdentifierType) {
      String receiverSymbolName = ((IdentifierType)receiverType).s;
      JavaSymbolTable.Symbol receiverSymbol = table.get(receiverSymbolName);

      if (receiverSymbol != null && receiverSymbol.getChildTable() != null) {
        symbol = receiverSymbol.getChildTable().get(n.i.s);
      }
    }

    if (symbol == null || !symbol.isCallable()) {
      errors.add(String.format("Attempt to call a non-method at line %d, character %d", 7, 7));
    }
    else if (((JavaSymbolTable.MethodSymbol)symbol).argumentsCount() != n.el.size()) {
      errors.add(String.format("Call to method %s does not match its declared number of arguments at line %d, character %d", n.i.s, 7, 7));
    }
    else {
      for (int i = 0; i < n.el.size(); i++) {
        Type actualType = n.el.elementAt(i).accept(this);
        Type formalType = ((JavaSymbolTable.MethodSymbol)symbol).getArguments().get(i);

        if (!actualType.equals(formalType)) {
          errors.add(String.format("Call to method %s does not match its declared signature at line %d, character %d", n.i.s, 7, 7));
        }
      }
    }

    if (symbol != null && symbol.isCallable()) {
      result = ((JavaSymbolTable.MethodSymbol)symbol).getReturnType();
    }

    return result;
  }

  // int i;
  public Type visit(IntegerLiteral n) {
    return new IntegerType();
  }

  public Type visit(True n) {
    return new BooleanType();
  }

  public Type visit(False n) {
    return new BooleanType();
  }

  // String s;
  public Type visit(IdentifierExp n) {
    Type result = null;
    JavaSymbolTable.Symbol symbol = table.get(n.s);

    if (symbol != null) {
      if (symbol instanceof JavaSymbolTable.VariableSymbol) {
        result = ((JavaSymbolTable.VariableSymbol)symbol).getType();
      }
      else {
        result = new IdentifierType(n.s);
      }
    }

    return result;
  }

  public Type visit(This n) {
    Type type = null;
    JavaSymbolTable.MethodSymbol symbol = (JavaSymbolTable.MethodSymbol)table.getParent();

    if (symbol != null) {
      if (symbol.getIsStatic()) {
        errors.add(String.format("Illegal use of keyword `this` in static method at line %d, character %d", 7, 7));
      }
      else {
        type = new IdentifierType(symbol.getName());
      }
    }

    return type;
  }

  // Exp e;
  public Type visit(NewArray n) {
    n.e.accept(this);
    return new IntArrayType();
  }

  // Identifier i;
  public Type visit(NewObject n) {
    Type result = null;
    JavaSymbolTable.Symbol symbol = table.get(n.i.s);

    if (symbol != null) {
      if (symbol.isInstantiatable()) {
        result = new IdentifierType(n.i.s);
      }
      else {
        errors.add(String.format("Attempt to instantiate a non-class at line %d, column %d", 7, 7));
      }
    }

    return result;
  }

  // Exp e;
  public Type visit(Not n) {
    n.e.accept(this);
    return new BooleanType();
  }

  // String s;
  public Type visit(Identifier n) {
    Type result = null;
    JavaSymbolTable.Symbol symbol = table.get(n.s);

    if (symbol != null && symbol instanceof JavaSymbolTable.VariableSymbol) {
      result = ((JavaSymbolTable.VariableSymbol)symbol).getType();
    }

    return result;
  }


  private void checkOperandReadability(String operator, Type leftType, Type rightType) {
    JavaSymbolTable.Symbol left, right;

    if (leftType instanceof IdentifierType
      && (left = table.get(((IdentifierType)leftType).s)) != null
      && !left.isReadable())
    {
      errors.add(String.format(
        "Invalid left operand for %s operator (%s is a %s) at line %d, character %d",
        operator,
        left.getName(), left.category(),
        left.getLine(), left.getColumn()
      ));
    }

    if (rightType instanceof IdentifierType
      && (right = table.get(((IdentifierType)rightType).s)) != null
      && !right.isReadable())
    {
      errors.add(String.format(
        "Invalid right operand for %s operator (%s is a %s) at line %d, character %d",
        operator,
        right.getName(), right.category(),
        right.getLine(), right.getColumn()
      ));
    }
  }
}
