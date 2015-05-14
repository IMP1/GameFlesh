package cls.trap;

import cls.Projectile;

public class BoulderTrap extends Trap {
	
	public final class Boulder extends Projectile {
		
		public final static int SPEED = 256;
		
		private double dx, dy;
		
		private Boulder(int x, int y, int dx, int dy) {
			super((x + 0.5) * cls.Level.TILE_SIZE, (y + 0.5) * cls.Level.TILE_SIZE);
			this.mass = 600;
			this.dx = dx * SPEED;
			this.dy = dy * SPEED;
		}
		
		public void update(double dt, scn.Map scene) {
			double newX = pixelX + (dx * dt);
			double newY = pixelY + (dy * dt);
			if (scene.isPixelPassable(newX, newY)) {
				pixelX = newX;
				pixelY = newY;
			} else {
				finished = true;
			}
		}
		
		public void draw() {
			jog.Graphics.setColour(255, 128, 128);
			if (((scn.Map)scn.SceneManager.scene()).isPixelVisible(pixelX, pixelY)) {
				jog.Graphics.circle(true, pixelX, pixelY, 8);
			}
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
		if (((scn.Map)scn.SceneManager.scene()).hasVisited(boulderX, boulderY)) {
			drawBoulderEnterPoint();
		}
	}
	
	private void drawBoulderEnterPoint() {
		int w = cls.Level.TILE_SIZE;
		int h = cls.Level.TILE_SIZE;
		jog.Graphics.setColour(128, 0, 0, 64);
		jog.Graphics.circle(true, (boulderX + 0.5) * w, (boulderY + 0.5) * h, w * 0.4);
	}
	
	@Override
	public boolean isAt(int i, int j) {
		return (
			i >= Math.min(triggerX, boulderX) && i <= Math.max(triggerX,  boulderX) &&
			j >= Math.min(triggerY, boulderY) && j <= Math.max(triggerY,  boulderY)
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
		if (triggerX < boulderX) dx = -1;
		if (triggerX > boulderX) dx = 1;
		if (triggerY < boulderY) dy = -1;
		if (triggerY > boulderY) dy = 1;
		if (scene.isTilePassable(ox - dx, oy - dy)) {
			ox -= dx;
			oy -= dy;
		}
		scene.addProjectile(new Boulder(ox, oy, dx, dy));
	}

}
