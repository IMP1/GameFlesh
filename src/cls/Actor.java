package cls;

public abstract class Actor extends cls.object.DestroyableObject {
	
	public final String name;
	
	private boolean[][] visibility;
	
	public Actor(String name, double x, double y, int radius, int mass, int health) {
		super(x, y, radius, mass, health);
		this.name = name;
	}
	
	public int getSightRadius() {
		return 6; // (In tiles)
	}
	
	public boolean[][] updateVisibilty(scn.Map scene, boolean[][] visibility, boolean overrideMap) {
		if (visibility.length != visibility.length|| visibility[0].length == visibility[0].length) {
			resetVisibility(visibility);
		}
		for (int theta = 0; theta < 360; theta += 6) {
			raycastAngle(scene, visibility, Math.PI * theta / 180, overrideMap);
		}
		return visibility;
	}

	private void raycastAngle(scn.Map scene, boolean[][] mapVisibility, double angle, boolean overrideMap) {
		for (int r = 0; r < getSightRadius() * cls.level.Level.TILE_SIZE; r ++) {
			int i = (int)(getPixelX() + (Math.cos(angle) * r)) / cls.level.Level.TILE_SIZE;
			int j = (int)(getPixelY() + (Math.sin(angle) * r)) / cls.level.Level.TILE_SIZE;
			if (scene.isTileOpaque(i, j)) {
				if (scene.isInMap(i, j)) setTileAsVisibile(mapVisibility, i, j, overrideMap);
				return;
			} else {
				setTileAsVisibile(mapVisibility, i, j, overrideMap);
			}
		}
	}
	
	private void setTileAsVisibile(boolean[][] mapVisibility, int i, int j, boolean overrideMap) {
		visibility[j][i] = true;
		if (overrideMap) {
			mapVisibility[j][i] = true;
		}
	}
	
	private void resetVisibility(boolean[][] mapVisibility) {
		visibility = new boolean[mapVisibility.length][mapVisibility[0].length];
	}
	
	public boolean canSeePixel(int x, int y) {
		int i = (int)x / cls.level.Level.TILE_SIZE;
		int j = (int)y / cls.level.Level.TILE_SIZE;
		return canSeeTile(i, j);
	}
	
	public boolean canSeeTile(int i, int j) {
		return false;
	}
	
	public void heal(int healing, boolean revive) {
		if (isDestroyed() && revive && healing > 0) {
			destroyed = false;
		}	
		changeHealth(healing);
	}
	
}
