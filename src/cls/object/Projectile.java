package cls.object;

public abstract class Projectile extends cls.object.ObjectWithMass {
	
	protected boolean finished;

	public Projectile(double x, double y) {
		pixelX = x;
		pixelY = y;
		finished = false;
	}

	public boolean isFinished() {
		return finished;
	}
	
	public abstract void update(double dt, scn.Map scene);
	
	public abstract void draw();
	
}
