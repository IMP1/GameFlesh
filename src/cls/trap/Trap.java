package cls.trap;

public abstract class Trap {
	
	protected int x, y;
	protected boolean triggered;
	public final String name;

	public Trap(int x, int y, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.triggered = false;
	}
	
	public void update(double dt) {}
	
	public void draw() {
		int w = cls.Level.TILE_SIZE;
		int h = cls.Level.TILE_SIZE;
		jog.Graphics.setColour(128, 0, 0, 128);
		jog.Graphics.rectangle(true, x * w, y * h, w, h);
	}
	
	public boolean isAt(int i, int j) {
		return (x == i && y == j);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public abstract void trigger(scn.Map scene);

}
