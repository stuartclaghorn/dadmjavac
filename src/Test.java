import java.util.*;
import ir.*;

public class Test {

	public static void main(String[] args) {
		List<T> irslist = new List<T>();
		System.out.println("Start test...");
		IRAssign ira = new IRAssign("string1","string2","string3");
		IRCopy irc = new IRCopy("string1","string2");
		irslist.add(ira);
		irslist.add(irc);

		System.out.println("End test.");
	}
}
