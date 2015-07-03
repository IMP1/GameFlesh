package cls.player;

import lib.gamepad.Gamepad;

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
		double dx = 0;
		double dy = 0;
		dx += controller.getLeftAxisHorizontal() * player.speed() * dt;
		dy += controller.getLeftAxisVertical() * player.speed() * dt;
		if (dx != 0 || dy != 0) {
			player.move(dx, dy);
		}
	}

}
