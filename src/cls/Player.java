package cls;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import scn.SceneManager;

public class Player extends cls.Actor {
	
	private static int playerCount = 0;
	
	private int playerID;
	private HashMap<Equipment.Slot, Equipment> equipment;

	public Player(int x, int y) {
		super("Player " + (playerCount + 1));
		playerID = playerCount;
		Player.playerCount ++;
		mass = 70;
		pixelX = (int)((x + 0.5) * Level.TILE_SIZE);
		pixelY = (int)((y + 0.5) * Level.TILE_SIZE);
		equipment = new HashMap<Equipment.Slot, Equipment>();
	}
	
	public void equip(Equipment item) {
		equipment.put(item.slot, item);
	}
	
	public void move(double dx, double dy) {
		double newX = pixelX + dx;
		double newY = pixelY + dy;
		if (((scn.Map)SceneManager.scene()).isPixelPassable(newX, newY)) {
			pixelX = newX;
			pixelY = newY;
		}
	}
	
	public void update(double dt) {
		if (playerID == 0) {
			double dx = 0;
			double dy = 0;
			if (jog.Input.isKeyDown(KeyEvent.VK_W)) {
				dy -= dt * 128;
			}
			if (jog.Input.isKeyDown(KeyEvent.VK_A)) {
				dx -= dt * 128;
			}
			if (jog.Input.isKeyDown(KeyEvent.VK_S)) {
				dy += dt * 128;
			}
			if (jog.Input.isKeyDown(KeyEvent.VK_D)) {
				dx += dt * 128;
			}
			if (dx != 0 || dy != 0) {
				move(dx, dy);
			}
		}
	}
	
	public void draw() {
		jog.Graphics.setColour(0, 255, 0);
		jog.Graphics.circle(true, pixelX, pixelY, 8);
	}

}

