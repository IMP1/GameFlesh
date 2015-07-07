package cls.enemy;

public abstract class Enemy extends cls.Actor {
	
	private static double getOriginalPosition(int i, int radius) {
		int w = cls.level.Level.TILE_SIZE - radius * 2;
		return i * cls.level.Level.TILE_SIZE + w * Math.random() / 2 + radius; 
	}

	public Enemy(String name, int x, int y, int r, int mass, int health) {
		super(name, getOriginalPosition(x, r), getOriginalPosition(y, r), Math.random() * Math.PI * 2, r, mass, health);
	}

	public void draw() {
		int i = (int)(pixelX / cls.level.Level.TILE_SIZE);
		int j = (int)(pixelY / cls.level.Level.TILE_SIZE);
		if (((scn.Map)scn.SceneManager.scene()).isTileVisible(i, j)) {
			jog.Graphics.setColour(256, 256, 256);
			jog.Graphics.circle(true, pixelX, pixelY, radius);
			jog.Graphics.setColour(0, 0, 0);
			jog.Graphics.circle(false, pixelX, pixelY, radius);
			jog.Graphics.line(pixelX, pixelY, pixelX + Math.cos(direction) * radius, pixelY + Math.sin(direction) * radius);
		}
	}

	@Override
	public String toString() {
		return name;
	}
	
}
