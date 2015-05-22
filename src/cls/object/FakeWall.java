package cls.object;

public class FakeWall extends DestroyableObject {

	int mapX, mapY;
	
	public FakeWall(int i, int j) {
		super((i + 0.5) * cls.Level.TILE_SIZE, (j + 0.5) * cls.Level.TILE_SIZE, cls.Level.TILE_SIZE / 2, Integer.MAX_VALUE, 100);
		mapX = i;
		mapY = j;
	}
	
	boolean isAtPixel(double x, double y) {
		return Math.abs(x - pixelX) <= radius && Math.abs(y - pixelY) <= radius;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		((scn.Map)scn.SceneManager.scene()).destroyFakeWall(mapX, mapY);
	}

}
