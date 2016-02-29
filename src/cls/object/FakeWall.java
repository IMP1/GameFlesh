package cls.object;

public class FakeWall extends DestroyableObject {

	int mapX, mapY;
	
	public FakeWall(int i, int j) {
		super((i + 0.5) * cls.level.Level.TILE_SIZE, (j + 0.5) * cls.level.Level.TILE_SIZE, cls.level.Level.TILE_SIZE / 2, Integer.MAX_VALUE, 100);
		mapX = i;
		mapY = j;
	}
	
	boolean isAtPixel(double x, double y) {
		return Math.abs(x - pixelX) <= radius && Math.abs(y - pixelY) <= radius;
	}
	
	public void destroy(boolean propogate) {
		super.destroy();
		scn.Map scene = (scn.Map)scn.SceneManager.scene();
		scene.destroyFakeWall(mapX, mapY, propogate);
	}
	
	@Override
	public void destroy() {
		destroy(true);
	}
	
	@Override
	public String toString() {
		return "Fake Wall (" + mapX + ", " + mapY + ")";
	}

}
