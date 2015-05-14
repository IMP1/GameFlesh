package cls.enemy;

public class Enemy extends cls.ObjectWithMass {
	
	public final String name;

	public Enemy(int x, int y, String name) {
		this.mass = 70;
		this.pixelX = (int)((x + Math.random()) * cls.Level.TILE_SIZE);
		this.pixelY = (int)((y + Math.random()) * cls.Level.TILE_SIZE);
		this.name = name;
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
