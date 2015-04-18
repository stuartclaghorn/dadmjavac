package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class MainClass extends Node {
  public Identifier i1,i2;
  public Statement s;

  public MainClass(Identifier ai1, Identifier ai2, Statement as, int l, int c) {
    i1=ai1; i2=ai2; s=as; line = l; column = c;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
