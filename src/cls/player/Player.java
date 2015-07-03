package cls.player;

import java.awt.event.KeyEvent;
import java.util.HashMap;

import cls.object.DestroyableObject;
import cls.equipment.Equipment;
import cls.level.Level;

import scn.SceneManager;

public class Player extends cls.Actor {
	
	private final static int RADIUS = 16;
	private final static int MASS   = 70;
	private final static int HEALTH = 100;
	private final static int MOVE_SPEED = 128;
	
	private static int playerCount = 0;
	
//	private int playerID;
	private InputHandler input;
	private HashMap<Equipment.Slot, Equipment> equipment;
	private boolean moving;
	private boolean rolling;
	private boolean attacking;

	public Player(InputHandler input) {
		super("Player " + (playerCount + 1), 0, 0, RADIUS, MASS, HEALTH);
//		playerID = playerCount;
		Player.playerCount ++;
		equipment = new HashMap<Equipment.Slot, Equipment>();
		this.input = input;
	}
	
	public int speed() {
		return MOVE_SPEED;
	}
	
	public void setStartPosition(int x, int y) {
		pixelX = (int)((x + 0.5) * Level.TILE_SIZE);
		pixelY = (int)((y + 0.5) * Level.TILE_SIZE);
	}
	
	public void equip(Equipment item) {
		equipment.put(item.slot, item);
	}
	
	public void move(double dx, double dy) {
		double newX = pixelX + dx;
		double newY = pixelY + dy;
		if (((scn.Map)SceneManager.scene()).isPixelPassable(newX, newY) || jog.Input.isKeyDown(KeyEvent.VK_CONTROL)) {
			pixelX = newX;
			pixelY = newY;
			moving = true;
		}
	}
	
	public void update(double dt) {
		input.updateInput(this, dt);
	}
	
	public void draw() {
		int opacity = isDestroyed() ? 64 : 255; 
		jog.Graphics.setColour(0, 255, 0, opacity);
		jog.Graphics.circle(true, pixelX, pixelY, radius);
		jog.Graphics.setColour(DestroyableObject.getHealthColor((double)currentHealth / maxHealth));
		String health = String.format("%d / %d", currentHealth, maxHealth);
		jog.Graphics.printCentred(health, pixelX, pixelY - radius * 2);
	}

}
