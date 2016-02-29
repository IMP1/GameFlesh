package run;

import lib.gamepad.GamepadManager;
import scn.Title;

public class Main extends run.Game {

	public static final boolean DEBUGGING = true;
	
	public Main() {
		super(new Title(), "Depths", 960, 640, 15);
	}
	
	@Override
	protected void setup() {
		GamepadManager.intitialise();
		Data.load();
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
