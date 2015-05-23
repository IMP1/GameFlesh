package cls.enemy;

public class SkeletonEnemy extends Enemy {
	
	private static final int RADIUS = 16;
	private static final int MASS   = 40;
	private static final int HEALTH = 100;

	public SkeletonEnemy(int i, int j) {
		super("Skeleton", i * cls.Level.TILE_SIZE, j * cls.Level.TILE_SIZE, RADIUS, MASS, HEALTH);
	}

}
