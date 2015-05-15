package cls;

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
	
	public void draw() {
		jog.Graphics.setColour(0, 255, 0);
		jog.Graphics.circle(true, pixelX, pixelY, 8);
	}

}

