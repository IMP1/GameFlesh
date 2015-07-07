package run;

import lib.gamepad.GamepadManager;
import scn.Scene;
import scn.Title;

public class Main extends run.Game {

	public static final boolean DEBUGGING = true;
	
	public Main() {
		super(new Title(), "Depths", 960, 640, 15);
	}
	
	@Override
	protected void setup(Scene startingScene) {
		GamepadManager.intitialise();
		Data.load();
		super.setup(startingScene);
	}
	
	@Override
	protected void updateInput() {
		super.updateInput();
		GamepadManager.pump();
	}
	
	public static void main(String[] args) {
		new Main();
	}
	
}
