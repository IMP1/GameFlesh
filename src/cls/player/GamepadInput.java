package cls.player;

import scn.SceneManager;
import lib.Camera;
import lib.gamepad.Gamepad;
import lib.gamepad.GamepadManager;

public class GamepadInput extends InputHandler implements GamepadManager.GamepadHandler {
	
	public static final int ROLL_BUTTON = Gamepad.A;
	public static final int ATTACK_BUTTON = Gamepad.X;
	public static final int LEVEL_EXIT_BUTTON = Gamepad.LB;

	Gamepad controller;
	
	public GamepadInput(Gamepad controller) {
		this.controller = controller;
		GamepadManager.addHandler(controller, this);
	}

	@Override
	protected void updateMovement(double dt) {
		double dx = 0;
		double dy = 0;
		dx += controller.getLeftAxisHorizontal() * player.speed() * dt;
		dy += controller.getLeftAxisVertical() * player.speed() * dt;
		if (dx != 0 || dy != 0) {
			player.move(dx, dy);
		}
	}
	
	protected void updateDirection(Camera camera) {
		double dx = 0;
		double dy = 0;
		dx += controller.getLeftAxisHorizontal();
		dy += controller.getLeftAxisVertical();
		if (dx != 0 || dy != 0) {
			player.faceTowards(Math.atan2(dy, dx));
		}
	}

	@Override
	public void buttonPressed(int button) {
		if (button == ROLL_BUTTON) {
			player.roll();
		}
		if (button == ATTACK_BUTTON) {
			player.attack();
		}
		if (button == LEVEL_EXIT_BUTTON) {
			if (player.isAtExit()) {
				((scn.Map)SceneManager.scene()).nextLevel();
			}
		}
	}

	@Override
	public void buttonReleased(int button) {}
	
}
