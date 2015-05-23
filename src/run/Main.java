package run;

import jog.Window.WindowMode;
import scn.Scene;
import scn.Title;

public class Main extends run.Game {

	public static final boolean DEBUGGING = false;
	
	public Main() {
		super(new Title(), "Depths", 960, 640, 15);
	}
	
	@Override
	protected void load(int width, int height, String title, Scene startingScene, WindowMode windowMode) {
		Data.load();
		super.load(width, height, title, startingScene, windowMode);
	}
	
	public static void main(String[] args) {
		new Main();
	}

}
