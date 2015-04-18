package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public abstract class Type extends Node {
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);

  public boolean equals(Type other) {
    return (other != null && other.getClass() == getClass());
  }
}
