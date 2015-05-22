package cls;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import cls.object.DestroyableObject;

import run.Main;
import scn.SceneManager;

public class Player extends cls.Actor {
	
	private final static int RADIUS = 16;
	private final static int MASS   = 70;
	private final static int HEALTH = 100;
	
	private static int playerCount = 0;
	
	private int playerID;
	private HashMap<Equipment.Slot, Equipment> equipment;

	public Player(int x, int y) {
		super("Player " + (playerCount + 1), x, y, RADIUS, MASS, HEALTH);
		playerID = playerCount;
		Player.playerCount ++;
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
		if (((scn.Map)SceneManager.scene()).isPixelPassable(newX, newY) || Main.DEBUGGING && jog.Input.isKeyDown(KeyEvent.VK_CONTROL)) {
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
		int opacity = isDestroyed() ? 64 : 255; 
		jog.Graphics.setColour(0, 255, 0, opacity);
		jog.Graphics.circle(true, pixelX, pixelY, radius);
		jog.Graphics.setColour(DestroyableObject.getHealthColor((double)currentHealth / maxHealth));
		String health = String.format("%d / %d", currentHealth, maxHealth);
		jog.Graphics.printCentred(health, pixelX - radius, pixelY - radius * 2, radius);
	}

}

