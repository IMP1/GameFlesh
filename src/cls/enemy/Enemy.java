package cls.enemy;

public abstract class Enemy extends cls.Actor {

	public Enemy(String name, int x, int y, int radius, int mass, int health) {
		super(name, x, y, radius, mass, health);
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
