package cls.trap;

public class SpikeTrap extends Trap {

	public SpikeTrap(int x, int y) {
		super(x, y, "Spike Trap", 1, 400);
	}

	@Override
	public void trigger(scn.Map scene) {
		
	}
	
	@Override
	public void draw() {
		if (((scn.Map)scn.SceneManager.scene()).hasVisited(triggerX, triggerY)) {
			int w = cls.Level.TILE_SIZE;
			int x = triggerX * w;
			int y = triggerY * w;
			jog.Graphics.setColour(64, 64, 64, 64);
			jog.Graphics.circle(true, x + 16, y + 8, 3);
			jog.Graphics.circle(true, x + 32, y + 8, 3);
			jog.Graphics.circle(true, x +  8, y + 16, 3);
			jog.Graphics.circle(true, x + 24, y + 16, 3);
			jog.Graphics.circle(true, x + 40, y + 16, 3);
			jog.Graphics.circle(true, x + 16, y + 24, 3);
			jog.Graphics.circle(true, x + 32, y + 24, 3);
			jog.Graphics.circle(true, x +  8, y + 32, 3);
			jog.Graphics.circle(true, x + 24, y + 32, 3);
			jog.Graphics.circle(true, x + 40, y + 32, 3);
			jog.Graphics.circle(true, x + 16, y + 40, 3);
			jog.Graphics.circle(true, x + 32, y + 40, 3);
		}
	}

}
