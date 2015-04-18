package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class Assign extends Statement {
  public Identifier i;
  public Exp e;

  public Assign(Identifier ai, Exp ae, int l, int c) {
    i=ai; e=ae; line = l; column = c;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
