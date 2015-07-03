package cls.player;

import java.awt.event.KeyEvent;

public class KeyboardMouseInput implements InputHandler {

	@Override
	public void updateInput(Player player, double dt) {
		updateMovement(player, dt);
	}
	
	private void updateMovement(Player player, double dt) {
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

}
