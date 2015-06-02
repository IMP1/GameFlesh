package run;

import scn.Scene;
import scn.Title;

public class Main extends run.Game {

	public static final boolean DEBUGGING = true;
	
	public Main() {
		super(new Title(), "Depths", 960, 640, 15);
	}
	
	@Override
	protected void setup(Scene startingScene) {
		Data.load();
		super.setup(startingScene);
	}
	
	public static void main(String[] args) {
		new Main();
	}

}
