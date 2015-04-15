package cls.trap;

import cls.Level;
import cls.Projectile;

public class BoulderTrap extends Trap {
	
	public final class Boulder extends Projectile {
		
		public final static int SPEED = 256;
		
		private double dx, dy;
		
		private Boulder(int x, int y, int dx, int dy) {
			super((x + 0.5) * Level.TILE_SIZE, (y + 0.5) * Level.TILE_SIZE);
			this.dx = dx * SPEED;
			this.dy = dy * SPEED;
		}
		
		public void update(double dt, scn.Map scene) {
			double newX = x + (dx * dt);
			double newY = y + (dy * dt);
			if (scene.isPassable(newX, newY)) {
				x = newX;
				y = newY;
			} else {
				finished = true;
			}
		}
		
		public void draw() {
			jog.Graphics.setColour(255, 128, 128);
			jog.Graphics.circle(true, x, y, 8);
		}
		
	}
	
	public final int boulderX;
	public final int boulderY;

	public BoulderTrap(int triggerX, int triggerY, int boulderX, int boulderY) {
		super(triggerX, triggerY, "Boulder Trap");
		this.boulderX = boulderX;
		this.boulderY = boulderY;
	}
	
	@Override
	public void draw() {
		super.draw();
		drawPressurePlate();
	}
	
	private void drawPressurePlate() {
		int w = cls.Level.TILE_SIZE;
		int h = cls.Level.TILE_SIZE;
		jog.Graphics.setColour(128, 0, 0, 64);
		jog.Graphics.circle(true, (boulderX + 0.5) * w, (boulderY + 0.5) * h, w * 0.4);
	}
	
	@Override
	public boolean isAt(int i, int j) {
		return (
			i >= Math.min(x, boulderX) && i <= Math.max(x,  boulderX) &&
			j >= Math.min(y, boulderY) && j <= Math.max(y,  boulderY)
		);
	}

	@Override
	public void trigger(scn.Map scene) {
		if (triggered) return;
		triggered = true;
		int ox = boulderX;
		int oy = boulderY;
		int dx = 0;
		int dy = 0;
		if (x < boulderX) dx = -1;
		if (x > boulderX) dx = 1;
		if (y < boulderY) dy = -1;
		if (y > boulderY) dy = 1;
		if (scene.isPassable(ox - dx, oy - dy)) {
			ox -= dx;
			oy -= dy;
		}
		scene.addProjectile(new Boulder(ox, oy, dx, dy));
	}

}
