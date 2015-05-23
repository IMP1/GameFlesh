package run;

import scn.Title;

public class Main extends run.Game {

	public static final boolean DEBUGGING = false;
	
	public Main() {
		super(new Title(), "Depths", 960, 640, 15);
	}
	
	public static void main(String[] args) {
		new Main();
	}

}
