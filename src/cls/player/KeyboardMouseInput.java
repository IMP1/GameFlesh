package cls.player;

import java.awt.event.KeyEvent;

public class KeyboardMouseInput extends InputHandler {

	@Override
	protected void updateMovement(Player player, double dt) {
		double dx = 0;
		double dy = 0;
		if (jog.Input.isKeyDown(KeyEvent.VK_W)) {
			dy -= dt * player.speed();
		}
		if (jog.Input.isKeyDown(KeyEvent.VK_A)) {
			dx -= dt * player.speed();
		}
		if (jog.Input.isKeyDown(KeyEvent.VK_S)) {
			dy += dt * player.speed();
		}
		if (jog.Input.isKeyDown(KeyEvent.VK_D)) {
			dx += dt * player.speed();
		}
		if (dx != 0 || dy != 0) {
			player.move(dx, dy);
		}
	}

	@Override
	protected void updateDirection(Player player) {
		// TODO: get mouse coordinates
		// TODO: possibly translate into game coordinates
		// TODO: player.faceTowards(Math.atan2(y, x));
		double x = jog.Input.getMouseX() - player.getScreenX();
		double y = jog.Input.getMouseY() - player.getScreenY();
		player.faceTowards(Math.atan2(y, x));
	}

}
