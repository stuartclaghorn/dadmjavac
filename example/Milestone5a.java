/** Recursion **/
class Test {
  public static void main(String[] args) {
    System.out.println(new Test2().Start(0));
  }
}
class Test2 {
  public int Start(int y) {
    int x;

    if (y < 10) {
      x = this.Start(y + 1);
      x = x + 1;
    }
    else {
      x = 0;
    }

    return x;
  }
}
