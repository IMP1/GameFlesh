package cls.trap;

import cls.object.DestroyableObject;
import lib.Animation;
import run.Cache;
import scn.Map;

public class SpikeTrap extends Trap {
	
	private final static jog.Image IMAGE = Cache.image("traps/spikes.png");
	private final static int DAMAGE = 50;
	
	private boolean hit;
	private Animation animation;

	public SpikeTrap(int x, int y) {
		super(x, y, "Spike Trap", 1, 400);
		animation = new Animation(IMAGE, 7, 1, 7, false, 0.04, 0.08, 0.16, 1, 0.1, 0.1, 0.5);
	}

	@Override
	public void update(double dt, Map scene) {
		super.update(dt, scene);
		if (animation.isPlaying()) {
			animation.update(dt);
			if (animation.isOnFrame(3) && !hit) {
				double x = (triggerX + 0.5) * cls.Level.TILE_SIZE;
				double y = (triggerY + 0.5) * cls.Level.TILE_SIZE;
				DestroyableObject obj = scene.getObjectAt(x, y, cls.Level.TILE_SIZE / 2);
				if (obj != null) obj.damage(DAMAGE);
				hit = true;
			}
		}
	}
	
	@Override
	public void trigger(scn.Map scene) {
		if (!animation.isPlaying()) {
			animation.reset();
			animation.start();
			hit = false;
		}
	}
	
	@Override
	public void draw() {
		if (((scn.Map)scn.SceneManager.scene()).hasVisited(triggerX, triggerY)) {
			int w = cls.Level.TILE_SIZE;
			int x = triggerX * w;
			int y = triggerY * w;
			if (!((scn.Map)scn.SceneManager.scene()).isTileVisible(triggerX, triggerY)) {
				jog.Graphics.setColour(255, 255, 255, 128);
			} else {
				jog.Graphics.setColour(255, 255, 255);
			}
			animation.draw(x, y);
		}
	}

}
