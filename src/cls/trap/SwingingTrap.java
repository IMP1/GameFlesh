package cls.trap;

public class SwingingTrap extends Trap {
	
	public enum Direction {
		HORIZONTAL,
		VERTICAL;
	}
	
	public final Direction direction;

	public SwingingTrap(int x, int y, Direction direction) {
		super(x, y, "Swinging Trap");
		this.direction = direction;
	}

	@Override
	public void trigger(scn.Map scene) {
		
	}
	
	@Override
	public void draw() {
		if (((scn.Map)scn.SceneManager.scene()).hasVisited(triggerX, triggerY)) {
			int w = cls.level.Level.TILE_SIZE;
			int h = cls.level.Level.TILE_SIZE;
			jog.Graphics.setColour(64, 64, 64, 64);
			if (direction == Direction.HORIZONTAL) {
				jog.Graphics.rectangle(true, triggerX * w, triggerY * h + 10, w, h - 20);
			} else {
				jog.Graphics.rectangle(true, triggerX * w + 10, triggerY * h, w - 20, h);
			}
			
		}
	}

}
