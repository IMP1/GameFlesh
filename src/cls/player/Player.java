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
	
	// Base Stats
	private final static int HEALTH = 100;
	private final static int MOVE_SPEED = 128;
	private final static int ROLL_SPEED = 256;
	private final static int ROLL_DISTANCE = 96;
	private final static double ATTACK_DURATION = 0.2;
	
	private static int playerCount = 0;
	
//	private int playerID;
	private InputHandler input;
	private HashMap<Equipment.Slot, Equipment> equipment;
	private boolean moving;
	private boolean rolling;
	private boolean attacking;
	private double rollTimer;
	private double attackTimer;

	public Player(InputHandler input) {
		super("Player " + (playerCount + 1), 0, 0, 0, RADIUS, MASS, HEALTH);
//		playerID = playerCount;
		Player.playerCount ++;
		equipment = new HashMap<Equipment.Slot, Equipment>();
		this.input = input;
		input.player = this;
		rollTimer = 0;
		attackTimer = 0;
	}
	
	public int speed() {
		return MOVE_SPEED;
	}
	
	public double attackDuration() {
		return ATTACK_DURATION;
	}
	
	public double attackDamage() {
		return 10;
	}
	
	public double rollSpeed() {
		return ROLL_SPEED;
	}
	
	public double rollDistance() {
		return ROLL_DISTANCE;
	}
	
	public double attackRange() {
		return 16;
	}
	
	public boolean isAtExit() {
		return ((scn.Map)SceneManager.scene()).isAtExit(getPixelX(), getPixelY());
	}
	
	public void setStartPosition(int x, int y) {
		pixelX = (int)((x + 0.5) * Level.TILE_SIZE);
		pixelY = (int)((y + 0.5) * Level.TILE_SIZE);
		direction = Math.PI / 2;
	}
	
	public void equip(Equipment item) {
		equipment.put(item.slot, item);
	}
	
	public void move(double dx, double dy) {
		if (rolling || attacking) return;
		changePosition(dx, dy);
	}
	
	public void faceTowards(double direction) {
		if (rolling || attacking) return;
		this.direction = direction;  
	}
	
	public void roll() {
		if (rolling) return;
		rollTimer = 0;
		rolling = true;
	}
	
	public void attack() {
		if (rolling || attacking) return;
		attackTimer = 0;
		attacking = true;
	}
	
	private void changePosition(double dx, double dy) {
		double newX = pixelX + dx;
		double newY = pixelY + dy;
		if (((scn.Map)SceneManager.scene()).isPixelPassable(newX, newY) || jog.Input.isKeyDown(KeyEvent.VK_CONTROL)) {
			pixelX = newX;
			pixelY = newY;
			moving = true;
		}
	}
	
	public void update(lib.Camera camera, double dt) {
		moving = false;
		input.updateInput(camera, dt);
		if (rolling) {
			updateRoll(dt);
		}
		if (attacking || attackTimer > 0) {
			updateAttack(dt);
		}
	}
	
	private void updateRoll(double dt) {
		rollTimer += dt;
		changePosition(rollSpeed() * dt * Math.cos(direction), rollSpeed() * dt * Math.sin(direction));
		if (rollTimer > rollDistance() / rollSpeed()) {
			rolling = false;
		}
	}
	
	private void updateAttack(double dt) {
		attackTimer += dt;
		if (attackTimer > attackDuration()) {
			scn.Map scene = (scn.Map)scn.SceneManager.scene();
			for (DestroyableObject obj : scene.getObjectsAt(pixelX, pixelY, (int)(radius + attackRange()))) {
				if (obj != this) {
					obj.damage((int)attackDamage());
				}
			}
			attacking = false;
		}
		if (!attacking) {
			attackTimer -= dt * 2;
			return;
		}
	}
	
	public void draw() {
		int opacity = isDestroyed() ? 64 : 255; 
		jog.Graphics.setColour(0, 255, 0, opacity);
		jog.Graphics.circle(true, pixelX, pixelY, radius);
		jog.Graphics.setColour(0, 0, 0, opacity);
		jog.Graphics.circle(false, pixelX, pixelY, radius);
		jog.Graphics.push();
		jog.Graphics.translate(pixelX, pixelY);
		jog.Graphics.rotate(direction);
		if (attacking) {
			jog.Graphics.circle(false, 0, 0, radius + attackRange());
			jog.Graphics.line(0, -6, 10, 6);
			jog.Graphics.line(0, 6, 10, -6);
		} else if (attackTimer > 0) {
			jog.Graphics.line(0, -6, 10, 6);
			jog.Graphics.line(0, 6, 10, -6);
		} else if (rolling) {
			jog.Graphics.polygon(true, 0, 6, 10, 0, 0, -6);
			jog.Graphics.polygon(true, 6, 6, 16, 0, 6, -6);
		} else if (moving) {
			jog.Graphics.polygon(true, 0, 6, 10, 0, 0, -6);
		} else {
			jog.Graphics.polygon(false, 0, 6, 10, 0, 0, -6);
		}
		jog.Graphics.pop();
		
		jog.Graphics.setColour(DestroyableObject.getHealthColor((double)currentHealth / maxHealth));
		String health = String.format("%d / %d", currentHealth, maxHealth);
		jog.Graphics.printCentred(health, pixelX, pixelY - radius * 2);
	}

}
