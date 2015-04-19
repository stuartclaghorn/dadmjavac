class Test {
  public static void main(String[] args) {
    System.out.println(new Test2().Start(0));
  }
}
class Test2 {
  public int Start(int y) {
    int x;
    int z;
    int[] i;
   
    i  = new int[4];

    x = y + 2;
    z = x * 7;
    y = z;

    return y;
  }
}
