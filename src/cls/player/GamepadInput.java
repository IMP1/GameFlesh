package cls.player;

import run.Gamepad;

public class GamepadInput implements InputHandler {

	Gamepad controller;
	
	public GamepadInput(Gamepad controller) {
		this.controller = controller;
	}

	@Override
	public void updateInput(Player player, double dt) {
		updateMovement(player, dt);
	}
	
	private void updateMovement(Player player, double dt) {
		
	}

}
