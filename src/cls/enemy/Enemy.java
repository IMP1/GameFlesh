package cls.enemy;

public class Enemy {
	
	protected int x, y;
	public final String name;

	public Enemy(int x, int y, String name) {
		this.x = (int)((x + Math.random()) * cls.Level.TILE_SIZE);
		this.y = (int)((y + Math.random()) * cls.Level.TILE_SIZE);
		this.name = name;
	}

	public void draw() {
		jog.Graphics.setColour(256, 256, 256);
		jog.Graphics.circle(true, x, y, 4);
		jog.Graphics.print("!", x + 4, y - 16);
	}
	
	public boolean isAt(int i, int j){
		return isAt(i, j, 0);
	}
	public boolean isAt(int i, int j, int leeway) {
		leeway += 16;
		int dx = i - x;
		int dy = j - y;
		return dx * dx + dy * dy <= leeway;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
