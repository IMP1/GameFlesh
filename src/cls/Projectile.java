package cls;

public abstract class Projectile extends ObjectWithMass {
	
	protected boolean finished;

	public Projectile(double x, double y) {
		this.pixelX = x;
		this.pixelY = y;
		finished = false;
	}

	public boolean isFinished() {
		return finished;
	}
	
	public abstract void update(double dt, scn.Map scene);
	
	public abstract void draw();
	
}
