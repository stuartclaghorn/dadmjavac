/** Conditional **/
class Test {
  public static void main(String[] args) {
    System.out.println(new Test2().Start(0));
  }
}
class Test2 {
  public int Start(int y) {
    int x;

    if (y < 10) {
      x = 5 + y;
    }
    else {
      x = 0;
    }

    return x;
  }
}
