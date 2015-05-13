package run;

import scn.Title;

public class Main extends run.Game {

	public Main() {
		super(new Title(), "Mnul", 960, 640, 15);
	}
	
	public static void main(String[] args) {
		new Main();
	}
	
	public static void puts(Object... args) {
		for (Object o : args) {
			System.out.print(o);
			System.out.print(" ");
		}
		System.out.print("\n");
	}

}
