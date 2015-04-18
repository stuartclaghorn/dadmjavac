import java_cup.runtime.Symbol;

%%

%class JavaLexer
%implements sym
%line
%column
%ignorecase
%eofclose
%cup

%{
  private int blockCommentDepth = 0;

  private void error(String message) {
    System.err.println("Error at line " + (yyline + 1) + ", column " + (yycolumn + 1) + " : " + message);
  }

  private Symbol symbol(int key) {
    return new Symbol(key, yyline + 1, yycolumn + 1);
  }
  private Symbol symbol(int key, Object value) {
    return new Symbol(key, yyline + 1, yycolumn + 1, value);
  }

  public int getLine() {
    return yyline;
  }

  public int getColumn() {
    return yycolumn;
  }
%}

identifier = [A-Za-z][A-Za-z0-9_]*
integer_literal = [0-9]+
newline = "\r" | "\n" | "\r\n"
whitespace = {newline} | [ \t]+

%state INLINE_COMMENT
%state BLOCK_COMMENT
%%

<YYINITIAL> {
  "//" { yybegin(INLINE_COMMENT); }
  "/*" { blockCommentDepth++; yybegin(BLOCK_COMMENT); }
  {whitespace} { }

  "System" { return symbol(SP_SYSTEM); }
  "out" { return symbol(SP_OUT); }
  "println" { return symbol(SP_PRINTLN); }
  "length" { return symbol(SP_LENGTH); }

  "class"   { return symbol(KW_CLASS); }
  "extends" { return symbol(KW_EXTENDS); }
  "new"     { return symbol(KW_NEW); }
  "this"    { return symbol(KW_THIS); }
  "public"  { return symbol(KW_PUBLIC); }
  "static"  { return symbol(KW_STATIC); }
  "void"    { return symbol(KW_VOID); }
  "main"    { return symbol(KW_MAIN); }
  "return"  { return symbol(KW_RETURN); }
  "if"      { return symbol(KW_IF); }
  "else"    { return symbol(KW_ELSE); }
  "while"   { return symbol(KW_WHILE); }

  "int"     { return symbol(T_INT); }
  "boolean" { return symbol(T_BOOLEAN); }
  "String"  { return symbol(T_STRING); }

  "[" { return symbol(LSBR); }
  "]" { return symbol(RSBR); }
  "{" { return symbol(LCBR); }
  "}" { return symbol(RCBR); }
  "(" { return symbol(LPAREN); }
  ")" { return symbol(RPAREN); }
  "." { return symbol(DOT); }
  ";" { return symbol(SEMI); }
  "," { return symbol(COMMA); }
  "=" { return symbol(EQUAL); }
  "!" { return symbol(NOT); }

  "true"  { return symbol(LIT_TRUE); }
  "false" { return symbol(LIT_FALSE); }
  {integer_literal} { return symbol(LIT_INT, Integer.parseInt(yytext())); }

  "&&" { return symbol(OP_AND); }
  "<"  { return symbol(OP_LT); }
  "+"  { return symbol(OP_PLUS); }
  "-"  { return symbol(OP_MINUS); }
  "*"  { return symbol(OP_TIMES); }

  {identifier} { return symbol(ID, yytext()); }

  [^] { error("Illegal symbol <" + yytext() + ">"); }
}

<INLINE_COMMENT> {
  {newline} { yybegin(YYINITIAL); }
  [^] { }
}
<BLOCK_COMMENT> {
  "/*" {
    blockCommentDepth++;
  }
  "*/" {
    blockCommentDepth--;

    if (blockCommentDepth == 0) {
      yybegin(YYINITIAL);
    }
  }
  [^] { }
}
