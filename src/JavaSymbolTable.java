import java.util.*;
import syntaxtree.*;

class JavaSymbolTable {
  private Map<String, List<Symbol>> symbols = new HashMap<String, List<Symbol>>();
  private JavaSymbolTable.Symbol parent = null;

  public JavaSymbolTable() {}
  public JavaSymbolTable(JavaSymbolTable.Symbol parent) {
    this.parent = parent;
  }

  public Map<String, List<Symbol>> getSymbols() { return symbols; }
  public Symbol getParent() { return parent; }

  public void add(Symbol symbol) {
    symbol.setTable(this);

    if (symbols.containsKey(symbol.getName())) {
      symbols.get(symbol.getName()).add(symbol);
    }
    else {
      ArrayList<Symbol> list = new ArrayList<Symbol>();
      list.add(symbol);
      symbols.put(symbol.getName(), list);
    }
  }

  public JavaSymbolTable addWithChild(Symbol symbol) {
    JavaSymbolTable child = new JavaSymbolTable(symbol);

    symbol.setChildTable(child);
    add(symbol);

    return child;
  }

  public JavaSymbolTable getChildFor(String name, int line, int column) {
    JavaSymbolTable result = null;

    if (symbols.containsKey(name)) {
      List<Symbol> potentialSymbols = symbols.get(name);
      Symbol symbol = null;

      for (Symbol potentialSymbol : potentialSymbols) {
        if (potentialSymbol.getLine() == line && potentialSymbol.getColumn() == column) {
          symbol = potentialSymbol;
          break;
        }
      }

      if (symbol != null) {
        result = symbol.getChildTable();
      }
    }

    return result;
  }

  public boolean contains(String name) {
    boolean result = symbols.containsKey(name);

    if (!result && parent != null && parent.getTable() != null) {
      result = parent.getTable().contains(name);
    }

    return result;
  }

  public Symbol get(String name) {
    Symbol result = null;

    if (symbols.containsKey(name) && symbols.get(name) != null && symbols.get(name).size() > 0) {
      result = symbols.get(name).get(0);
    }
    else if (parent != null && parent.getTable() != null) {
      result = parent.getTable().get(name);
    }

    return result;
  }

  public Symbol get(String name, int line, int column) {
    Symbol result = null;

    if (symbols.containsKey(name) && symbols.get(name) != null) {
      for (Symbol potentialSymbol : symbols.get(name)) {
        if (potentialSymbol.getLine() == line && potentialSymbol.getColumn() == column) {
          result = potentialSymbol;
          break;
        }
      }
    }

    if (result == null && parent != null && parent.getTable() != null) {
      result = parent.getTable().get(name, line, column);
    }

    return result;
  }


  public static class Symbol {
    private String name;
    private JavaSymbolTable table = null;
    private JavaSymbolTable childTable = null;
    private int line, column;

    public Symbol(String name, int line, int column) {
      this.name = name;
      this.line = line;
      this.column = column;
    }

    public String getName() { return name; }

    public JavaSymbolTable getTable() { return table; }
    public void setTable(JavaSymbolTable table) { this.table = table; }
    public JavaSymbolTable getChildTable() { return childTable; }
    public void setChildTable(JavaSymbolTable childTable) { this.childTable = childTable; }

    public int getLine() { return line; }
    public int getColumn() { return column; }

    public boolean isWritable() { return false; }
    public boolean isReadable() { return false; }
    public boolean isCallable() { return false; }
    public boolean isInstantiatable() { return false; }

    public String category() { return ""; }
  }

  public static class ClassSymbol extends Symbol {
    public ClassSymbol(String name, int line, int column) {
      super(name, line, column);
    }

    public boolean isInstantiatable() { return true; }

    public String category() { return "class"; }
  }

  public static class MethodSymbol extends Symbol {
    private boolean isStatic = false;
    private List<Type> arguments = new ArrayList<Type>();
    private Type returnType;

    public MethodSymbol(String name, Type returnType, int line, int column) {
      super(name, line, column);
      this.returnType = returnType;
    }

    public Type getReturnType() { return returnType; }

    public List<Type> getArguments() { return arguments; }
    public void addArgument(Type type) { arguments.add(type); }
    public int argumentsCount() { return arguments.size(); }

    public void setIsStatic(boolean isStatic) { this.isStatic = isStatic; }
    public boolean getIsStatic() { return isStatic; }

    public boolean isCallable() { return true; }

    public String category() { return "method"; }
  }

  public static class VariableSymbol extends Symbol {
    private Type type;

    public VariableSymbol(String name, Type type, int line, int column) {
      super(name, line, column);
      this.type = type;
    }

    public Type getType() { return type; }

    public boolean isWritable() { return true; }
    public boolean isReadable() { return true; }

    public String category() { return "variable"; }
  }
}
