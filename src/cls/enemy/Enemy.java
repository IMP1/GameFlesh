package cls.enemy;

public abstract class Enemy extends cls.Actor {

	public Enemy(int x, int y, String name) {
		super(name);
		mass = 70;
		pixelX = (int)((x + Math.random()) * cls.Level.TILE_SIZE);
		pixelY = (int)((y + Math.random()) * cls.Level.TILE_SIZE);
	}

	public void draw() {
		int i = (int)(pixelX / cls.Level.TILE_SIZE);
		int j = (int)(pixelY / cls.Level.TILE_SIZE);
		if (((scn.Map)scn.SceneManager.scene()).isTileVisible(i, j)) {
			jog.Graphics.setColour(256, 256, 256);
			jog.Graphics.circle(true, pixelX, pixelY, 4);
			jog.Graphics.print("!", pixelX + 4, pixelY - 16);
		}
	}

	@Override
	public String toString() {
		return name;
	}
	
}
