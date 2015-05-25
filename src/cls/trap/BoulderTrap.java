package cls.trap;

import lib.Screenshake;
import lib.Sprite;
import run.Cache;
import run.Main;
import scn.Map;
import cls.object.DestroyableObject;
import cls.object.Projectile;

public class BoulderTrap extends Trap {
	
	private final static jog.Image boulderImage = Cache.loadImage("gfx/traps/boulder.png");
	private final static int boulderRumble = 1;
	private final static int boulderHitShake = 3;
	
	public final class Boulder extends Projectile {
		
		public final static int SPEED = 256;
		
		private Sprite animation;
		
		private Boulder(int x, int y, int dx, int dy) {
			super((x + 0.5) * cls.Level.TILE_SIZE, (y + 0.5) * cls.Level.TILE_SIZE, 16, 600, 9999);
			animation = new Sprite(boulderImage, 4, 4, 0.2);
			int direction = 0;
			direction += dx == 0 ?  1 :  0;
			direction += dy == 0 ?  2 :  0;
			direction += dy < 0  ?  1 :  0;
			direction += dy > 0  ? -1 :  0;
			direction += dx < 0  ? -1 :  0;
			direction += dx > 0  ?  1 :  0;
			animation.setPose(direction);
			mass = 600;
			vx = dx * SPEED;
			vy = dy * SPEED;
			Screenshake.addRumble(boulderRumble, boulderRumble);
		}
		
		@Override
		protected void destroy() {
			Screenshake.removeRumble(boulderRumble, boulderRumble);
			Screenshake.addScreenshake(boulderHitShake, boulderHitShake, 0.2);
			super.destroy();
		}
		
		@Override
		public void update(double dt, Map scene) {
			super.update(dt, scene);
			animation.update(dt);
		}
		
		@Override
		public void hit(DestroyableObject obj) {
			obj.damage(maxDamage);
		}
		
		public void draw() {
			if (((scn.Map)scn.SceneManager.scene()).isPixelVisible(pixelX, pixelY)) {
				animation.draw(pixelX - radius, pixelY - radius);
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
		if (((scn.Map)scn.SceneManager.scene()).hasVisited(boulderX, boulderY) && Main.DEBUGGING) {
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
