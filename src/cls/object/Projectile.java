package cls.object;

public abstract class Projectile extends cls.object.ObjectWithMass {
	
	protected boolean finished;
	protected double vx;
	protected double vy;
	protected int maxDamage;

	public Projectile(double x, double y, int radius, int mass, int damage) {
		super(x, y, radius, mass);
		finished = false;
		vx = 0;
		vy = 0;
		maxDamage = damage;
	}

	public boolean isFinished() {
		return finished;
	}
	
	protected boolean canMoveThrough(scn.Map scene, double x, double y) {
		if (!scene.isPixelPassable(x, y)) return false; 
		if (scene.getObjectAt(x, y, radius) != null) return false;
		return true;
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
	 * Hit something static, like a wall.
	 */
	protected void hit() {
		destroy();
	}
	
	protected void destroy() {
		finished = true;
	}
	
	public void update(double dt, scn.Map scene) {
		double newX = pixelX + (vx * dt);
		double newY = pixelY + (vy * dt);
		if (canMoveThrough(scene, newX, newY)) {
			pixelX = newX;
			pixelY = newY;
		} else {
			DestroyableObject obj = scene.getObjectAt(newX, newY, radius);
			if (obj == null) {
				hit();
			} else {
				hit(obj);
			}
		}
	}
	
	public abstract void draw();
	
}
