package cls;

public abstract class Projectile {
	
	protected double x;
	protected double y;
	protected boolean finished;

	public Projectile(double x, double y) {
		this.x = x;
		this.y = y;
		finished = false;
	}

	public boolean isFinished() {
		return finished;
	}
	
	public abstract void update(double dt, scn.Map scene);
	
	public abstract void draw();
	
}
