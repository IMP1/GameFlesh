package cls.trap;

public class BoulderTrap extends Trap {
	
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

}
