package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public abstract class Exp extends Node {
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
}
