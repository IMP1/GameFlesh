package cls.player;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import scn.SceneManager;

import lib.Camera;

public class KeyboardMouseInput extends InputHandler implements jog.Event.KeyboardEventHandler, jog.Event.MouseEventHandler {

	public static final int ROLL_KEY = KeyEvent.VK_SPACE;
	public static final int LEVEL_EXIT_KEY = KeyEvent.VK_SHIFT;
	
	public KeyboardMouseInput() {
		jog.Event.addKeyboardHandler(this);
		jog.Event.addMouseHandler(this);
	}
	
	@Override
	protected void updateMovement(double dt) {
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
	protected void updateDirection(Camera camera) {
		double mx = camera.getMouseWorldX();
		double my = camera.getMouseWorldY();
		double x = mx - player.getPixelX();
		double y = my - player.getPixelY();
		player.faceTowards(Math.atan2(y, x));
	}

	@Override
	public void keyPressed(int key) {
		if (key == ROLL_KEY) {
			player.roll();
		}
		if (key == LEVEL_EXIT_KEY) {
			if (player.isAtExit()) {
				((scn.Map)SceneManager.scene()).nextLevel();
			}
		}
	}

	@Override
	public void mousePressed(int x, int y, int key) {
		if (key == MouseEvent.BUTTON1) {
			player.attack();
		}
	}

	@Override
	public void keyReleased(int key) {}

	@Override
	public void mouseMoved(int x, int y) {}

	@Override
	public void mouseScrolled(int x, int y, int scroll) {}

	@Override
	public void mouseReleased(int x, int y, int key) {}

	@Override
	public void keyTyped(char text) {}

}
