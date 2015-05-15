package cls.trap;

import cls.object.Projectile;

public class ArrowTrap extends Trap {
	
	public final class Arrow extends Projectile {
		
		public final static int SPEED = 512;
		
		private double dx, dy;
		
		private Arrow(int x, int y, int dx, int dy) {
			super((x + 0.5) * cls.Level.TILE_SIZE, (y + 0.5) * cls.Level.TILE_SIZE);
			this.mass = 600;
			this.dx = dx * SPEED;
			this.dy = dy * SPEED;
		}
		
		public void update(double dt, scn.Map scene) {
			double newX = pixelX + (dx * dt);
			double newY = pixelY + (dy * dt);
			int i = (int)newX / cls.Level.TILE_SIZE;
			int j = (int)newY / cls.Level.TILE_SIZE;
			if (scene.isPixelPassable(newX, newY) || (i == ballistaX && j == ballistaY)) {
				pixelX = newX;
				pixelY = newY;
			} else {
				finished = true;
			}
		}
		
		public void draw() {
			jog.Graphics.setColour(255, 255, 255);
			if (((scn.Map)scn.SceneManager.scene()).isPixelVisible(pixelX, pixelY)) {
				jog.Graphics.circle(true, pixelX, pixelY, 8);
			}
		}
		
	}
	
	public static final double COOLDOWN = 1.0;
	
	public final int ballistaX;
	public final int ballistaY;
	public int arrowsRemaining;
	public double cooldownTimer;

	public ArrowTrap(int x, int y, int ballistaX, int ballistaY) {
		super(x, y, "Arrow Trap");
		this.ballistaX = ballistaX;
		this.ballistaY = ballistaY;
		arrowsRemaining = 5;
		cooldownTimer = 0;
	}
	
	@Override
	public void update(double dt, scn.Map scene) {
		super.update(dt, scene);
		if (cooldownTimer > 0) {
			cooldownTimer = Math.max(0, cooldownTimer - dt); 
		}
	}
	
	@Override
	public void draw() {
		super.draw();
		if (((scn.Map)scn.SceneManager.scene()).hasVisited(ballistaX, ballistaY)) {
			int w = cls.Level.TILE_SIZE;
			int h = cls.Level.TILE_SIZE;
			jog.Graphics.setColour(128, 0, 0, 64);
			jog.Graphics.push();
			jog.Graphics.translate((ballistaX + 0.5) * w, (ballistaY + 0.5) * h);
			int x1, y1, x2, y2, x3, y3, x4, y4;
			x1 = 0;
			y1 = -h / 2;
			x2 = w / 2;
			y2 = 0;
			x3 = 0;
			y3 = h / 2;
			x4 = -w / 2;
			y4 = 0;
			jog.Graphics.polygon(true, x1, y1, x2, y2, x3, y3, x4, y4);
			jog.Graphics.pop();
		}
	}

	@Override
	public boolean isAt(int i, int j) {
		return (
			i >= Math.min(triggerX, ballistaX) && i <= Math.max(triggerX,  ballistaX) &&
			j >= Math.min(triggerY, ballistaY) && j <= Math.max(triggerY,  ballistaY)
		);
	}

	@Override
	public void trigger(scn.Map scene) {
		if (arrowsRemaining <= 0) return;
		if (cooldownTimer > 0) return;
		triggered = true;
		cooldownTimer = COOLDOWN;
		arrowsRemaining --;
		int ox = ballistaX;
		int oy = ballistaY;
		int dx = 0;
		int dy = 0;
		if (triggerX < ballistaX) dx = -1;
		if (triggerX > ballistaX) dx = 1;
		if (triggerY < ballistaY) dy = -1;
		if (triggerY > ballistaY) dy = 1;
		scene.addProjectile(new Arrow(ox, oy, dx, dy));
	}
	
}
