package cls.trap;

public class ArrowTrap extends Trap {
	
	public final int ballistaX;
	public final int ballistaY;

	public ArrowTrap(int x, int y, int ballistaX, int ballistaY) {
		super(x, y, "Arrow Trap");
		this.ballistaX = ballistaX;
		this.ballistaY = ballistaY;
	}
	
	@Override
	public void draw() {
		super.draw();
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

	@Override
	public boolean isAt(int i, int j) {
		return (
			i >= Math.min(x, ballistaX) && i <= Math.max(x,  ballistaX) &&
			j >= Math.min(y, ballistaY) && j <= Math.max(y,  ballistaY)
		);
	}

	@Override
	public void trigger(scn.Map scene) {
		
	}
	
}
