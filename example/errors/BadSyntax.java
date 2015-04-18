class Factorial {
  // Main class body error
  //where is my mind

  public static void main(String[] a) {
    System.out.println(new Fac().ComputeFac(10));
  }
}
class Fac {
  // Simple class body error
  //where is my mind

  public int ComputeFac(int num) {
    // Variable declaration error
    int 7 = foo;

    int num_aux;
    if (num < 1)
      num_aux = 1;
    else
      num_aux = num * (this.ComputeFac(num-1));

    // Actual params error
    asdf = foo.bar(asdf 123);

    return num_aux;
  }

  // Formal params error
  public int BadMethod(asdfasdf) {
    int num_aux;
    if (num < 1)
      num_aux = 1;
    else
      num_aux = num * (this.ComputeFac(num-1));
    return num_aux;
  }

  // Method body error (missing return)
  public int OtherBadMethod() {
    int num;
    num = 5;
    System.out.println(num);
  }
}
class Fac extends Math {
  // Class body error
  where is my mind
}
