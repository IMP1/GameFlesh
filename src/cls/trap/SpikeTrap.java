package cls.trap;

import cls.object.DestroyableObject;
import lib.Animation;
import run.Cache;
import scn.Map;

public class SpikeTrap extends Trap {
	
	private final static jog.Image IMAGE = Cache.loadImage("gfx/traps/spikes.png");
	private final static int DAMAGE = 50;
	
	private Animation animation;

	public SpikeTrap(int x, int y) {
		super(x, y, "Spike Trap", 1, 400);
		animation = new Animation(IMAGE, 7, 1, 7, false, 0, 0, 0, 1, 0.1, 0.1, 0.5);
	}

	@Override
	public void update(double dt, Map scene) {
		super.update(dt, scene);
		animation.update(dt);
	}
	
	@Override
	public void trigger(scn.Map scene) {
		if (!animation.isPlaying()) {
			animation.reset();
			animation.start();
			DestroyableObject obj = scene.getObjectAt(triggerX, triggerY, 32);
			if (obj != null) obj.damage(DAMAGE);
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
