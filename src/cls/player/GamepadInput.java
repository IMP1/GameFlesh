package cls.player;

import lib.gamepad.Gamepad;
import lib.gamepad.GamepadManager;

public class GamepadInput extends InputHandler implements GamepadManager.GamepadHandler {

	Gamepad controller;
	
	public GamepadInput(Gamepad controller) {
		this.controller = controller;
		GamepadManager.addHandler(controller, this);
	}

	@Override
	protected void updateMovement(Player player, double dt) {
		double dx = 0;
		double dy = 0;
		dx += controller.getLeftAxisHorizontal() * player.speed() * dt;
		dy += controller.getLeftAxisVertical() * player.speed() * dt;
		if (dx != 0 || dy != 0) {
			player.move(dx, dy);
		}
	}
	
	protected void updateDirection(Player player) {
		double x = controller.getRightAxisHorizontal();
		double y = controller.getRightAxisVertical();
		if (x != 0 || y != 0) {
			player.faceTowards(Math.atan2(y, x));
		}
	}

	@Override
	public void buttonPressed(int button) {
		System.out.println(button);
	}

	@Override
	public void buttonReleased(int button) {
		System.out.println(button);
	}
	
	
	
}
