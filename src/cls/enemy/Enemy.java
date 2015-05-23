package cls.enemy;

public abstract class Enemy extends cls.Actor {
	
	private static double getOriginalPosition(int i, int radius) {
		int w = cls.Level.TILE_SIZE - radius * 2;
		return i * cls.Level.TILE_SIZE + w * Math.random() / 2 + radius; 
	}

	public Enemy(String name, int x, int y, int r, int mass, int health) {
		super(name, getOriginalPosition(x, r), getOriginalPosition(y, r), r, mass, health);
	}

	public void draw() {
		int i = (int)(pixelX / cls.Level.TILE_SIZE);
		int j = (int)(pixelY / cls.Level.TILE_SIZE);
		if (((scn.Map)scn.SceneManager.scene()).isTileVisible(i, j)) {
			jog.Graphics.setColour(256, 256, 256);
			jog.Graphics.circle(true, pixelX, pixelY, radius);
			jog.Graphics.print("!", pixelX + 4, pixelY - 16);
		}
	}

	@Override
	public String toString() {
		return name;
	}
	
}
