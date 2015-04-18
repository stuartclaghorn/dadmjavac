/** Loop **/
class Test {
  public static void main(String[] args) {
    System.out.println(new Test2().Start(0));
  }
}
class Test2 {
  public int Start(int y) {
    int x;

    x = y;

    while (x < 10) {
      x = x + 1;
    }

    return x;
  }
}
