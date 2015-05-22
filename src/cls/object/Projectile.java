package cls.object;

public abstract class Projectile extends cls.object.ObjectWithMass {
	
	protected boolean finished;
	protected double vx;
	protected double vy;
	protected int maxDamage;

	public Projectile(double x, double y) {
		pixelX = x;
		pixelY = y;
		finished = false;
		vx = 0;
		vy = 0;
		maxDamage = 10;
	}

	public boolean isFinished() {
		return finished;
	}
	
	protected boolean canMoveThrough(scn.Map scene, double x, double y) {
		return scene.isPixelPassable(x, y);
	}
	
	/**
	 * Hit a destroyable object.
	 * @param obj the object hit.
	 */
	protected void hit(DestroyableObject obj) {
		obj.damage(maxDamage);
		destroy();
	}
	
	/**
	 * Hit an actor.
	 * @param actor the actor hit.
	 */
	protected void hit(cls.Actor actor) {
		actor.damage(maxDamage);
		hit();
	}
	
	/**
	 * Hit something static, like a wall.
	 */
	protected void hit() {
		destroy();
	}
	
	private void destroy() {
		finished = true;
	}
	
	public void update(double dt, scn.Map scene) {
		double newX = pixelX + (vx * dt);
		double newY = pixelY + (vy * dt);
		if (canMoveThrough(scene, newX, newY)) {
			pixelX = newX;
			pixelY = newY;
		} else {
			hit();
		}
	}
	
	public abstract void draw();
	
}
