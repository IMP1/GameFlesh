package cls;

public abstract class Actor extends cls.object.ObjectWithMass {
	
	public final String name;
	
	protected int maxHealth;
	protected int currentHealth;
	protected boolean incapacitated;
	
	private boolean[][] visibility;
	
	public Actor(String name) {
		this.name = name;
	}

	/**
	 * Reduces the objects health.
	 * @param damage the amount of damage the attacker is giving out.
	 * @return the amount of damage that was actually dealt.
	 */
	public int damage(int damage) {
		currentHealth = currentHealth - damage;
		if (currentHealth <= 0) {
			incapacitated = true;
			currentHealth = 0;
		}
		return damage;
	}
	
	public boolean isDestroyed() {
		return incapacitated;
	}
	
	public int getSightRadius() {
		return 6; // (In tiles)
	}
	
	public boolean[][] updateVisibilty(scn.Map scene, boolean[][] visibility, boolean overrideMap) {
		if (visibility.length != visibility.length|| visibility[0].length == visibility[0].length) {
			resetVisibility(visibility);
		}
		for (int theta = 0; theta < 360; theta ++) {
			raycastAngle(scene, visibility, Math.PI * theta / 180, overrideMap);
		}
		return visibility;
	}

	private void raycastAngle(scn.Map scene, boolean[][] mapVisibility, double angle, boolean overrideMap) {
		for (int r = 0; r < getSightRadius() * Level.TILE_SIZE; r ++) {
			int i = (int)(getPixelX() + (Math.cos(angle) * r)) / Level.TILE_SIZE;
			int j = (int)(getPixelY() + (Math.sin(angle) * r)) / Level.TILE_SIZE;
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
		int i = (int)x / cls.Level.TILE_SIZE;
		int j = (int)y / cls.Level.TILE_SIZE;
		return canSeeTile(i, j);
	}
	
	public boolean canSeeTile(int i, int j) {
		return false;
	}
	
}
