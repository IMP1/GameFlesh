package cls.enemy;

public class SkeletonEnemy extends Enemy {
	
	private static final int RADIUS = 16;
	private static final int MASS   = 40;
	private static final int HEALTH = 100;

	public SkeletonEnemy(int x, int y) {
		super("Skeleton", x, y, RADIUS, MASS, HEALTH);
	}

}
