class Factorial {
  public static void main(String[] a) {
    // Use of this in a static method
    System.out.println(this.ComputeFac(10));
  }
}
class Fac {
  int asdf;

  public int ComputeFac(int num) {
    int num_aux;
    Fac fac;
    int[] numbers;
    boolean whargarbl;

    if (num < 1)
      num_aux = 1;
    else
      num_aux = num * (this.ComputeFac(num-1));

    ComputeFac = 187;
    Fac = 187;
    num_aux = ComputeFac;
    foo = Fac;

    // Attempt to use an unreadable symbol as an operand
    if (num < ComputeFac)
      num_aux = 1;
    else
      num_aux = 2;

    // Call to non-method
    num_aux = (true && false).asdf();
    num_aux = (5 < 3).asdf();
    num_aux = (5 + 3).asdf();
    num_aux = (5 - 3).asdf();
    num_aux = (5 * 3).asdf();
    num_aux = numbers[7].asdf();
    num_aux = numbers.length.asdf();
    //num_aux = this.ComputeFac(7).asdf(); pending return type checking
    num_aux = 56.asdf();
    num_aux = true.asdf();
    num_aux = false.asdf();
    num_aux = fac.asdf();
    num_aux = this.asdf();
    num_aux = (new int[7]).asdf();
    num_aux = new Fac().asdf();
    num_aux = (!false).asdf();

    // Not a call to a non-method
    num_aux = this.ComputeFac(100);

    // Invalid number of arguments
    num_aux = this.ComputeFac();
    num_aux = this.ComputeFac(3, 5);

    // Invalid call signature
    num_aux = this.ComputeFac(true);
    num_aux = this.ComputeFac(new int[7]);
    num_aux = this.ComputeFac(new Fac());

    // Instantiating a non-class
    num_aux = new ComputeFac();
    num_aux = new whargarbl();

    // Non-integer operand for integer operator
    num_aux = true + 5;
    num_aux = 5 * true;
    num_aux = true - 5;
    whargarbl = true < 5;
    num_aux = whargarbl + 7;

    // Integer operand for integer operator
    num_aux = 5 + 3;
    num_aux = 7 + num_aux;

    // Boolean operator with non-boolean operands
    whargarbl = 5 && 7;
    whargarbl = true && num_aux;
    whargarbl = num_aux && true;

    // Boolean operator with boolean operands
    whargarbl = whargarbl && true;
    whargarbl = true && whargarbl;

    // Length property on non-array
    num_aux = 7.length;
    num_aux = true.length;
    num_aux = num_aux.length;
    num_aux = whargarbl.length;
    num_aux = (new Fac()).length;

    // Length property on array
    num_aux = (new int[7]).length;

    // Non-boolean condition for if statement
    if (5) num_aux = 1; else num_aux = 2;
    if (numbers) num_aux = 1; else num_aux = 2;
    if (num_aux) num_aux = 1; else num_aux = 2;

    // Boolean condition for if statement
    if (true) num_aux = 1; else num_aux = 2;
    if (whargarbl) num_aux = 1; else num_aux = 2;

    // Non-boolean condition for while statement
    while (5) num_aux = 1;
    while (numbers) num_aux = 1;
    while (num_aux) num_aux = 1;

    // Boolean condition for while statement
    while (true) num_aux = 1;
    while (whargarbl) num_aux = 1;

    // Mismatched assignment
    num_aux = true;
    num_aux = new int[7];
    num_aux = whargarbl;
    whargarbl = 7;
    whargarbl = new int[7];
    whargarbl = num_aux;

    // Wrong arg to System.out.println
    System.out.println(new int[7]);
    System.out.println(true);
    System.out.println(whargarbl);

    // Good arg to System.out.println
    System.out.println(7);
    System.out.println(num_aux);

    return num_aux;
  }
}
