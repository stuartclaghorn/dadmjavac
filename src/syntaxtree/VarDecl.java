package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class VarDecl extends Node {
  public Type t;
  public Identifier i;

  public VarDecl(Type at, Identifier ai, int l, int c) {
    t = at; i = ai; line = l; column = c;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
