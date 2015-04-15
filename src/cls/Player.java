package cls;

import java.awt.Point;
import java.util.HashMap;

import scn.SceneManager;

public class Player {
	
	private static int playerCount = 0;
	
	public final String name;
	
	private int playerID;
	private double x;
	private double y;
	
	private int agility;
	private int dexterity;
	private int vitality;
	private int strength;
	//
	private int defence;
	private int speed;
	private int balance;
	
	private HashMap<Equipment.Slot, Equipment> equipment;

	public Player(int x, int y) {
		playerID = playerCount ++;
		this.x = (x + 0.5) * Level.TILE_SIZE;
		this.y = (y + 0.5) * Level.TILE_SIZE;
		this.name = "Player " + (playerID + 1);
		this.equipment = new HashMap<Equipment.Slot, Equipment>();
	}
	
	public void equip(Equipment item) {
		equipment.put(item.slot, item);
	}
	
	public void move(double dx, double dy) {
		double newX = x + dx;
		double newY = y + dy;
		if (((scn.Map)SceneManager.scene()).isPassable(newX, newY)) {
			x = newX;
			y = newY;
		}
	}
	
	public void draw() {
		jog.Graphics.setColour(0, 255, 0);
		jog.Graphics.circle(true, x, y, 8);
	}

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
}

