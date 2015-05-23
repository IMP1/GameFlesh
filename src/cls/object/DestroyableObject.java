package cls.object;

public abstract class DestroyableObject extends ObjectWithMass {

	protected int maxHealth;
	protected int currentHealth;
	protected boolean destroyed;

	public DestroyableObject(double x, double y, int radius, int mass, int health) {
		super(x, y, radius, mass);
		maxHealth = health;
		currentHealth = maxHealth;
		destroyed = false;
	}
	
	/**
	 * Reduces the objects health.
	 * @param damage the amount of damage the attacker is giving out.
	 * @return the amount of damage that was actually dealt.
	 */
	public int damage(int damage) {
		currentHealth = currentHealth - damage;
		if (currentHealth <= 0) {
			destroy();
		}
		return damage;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void destroy() {
		destroyed = true;
		currentHealth = 0;
	}
	
	public static java.awt.Color getHealthColor(double health) {
		int r = (int)(255 * 2 * (1 - health));
		int g = (int)(255 * 2 * health);
		if (r > 255) r = 255;
		if (g > 255) g = 255;
		return new java.awt.Color(r, g, 0);
	}
	
}
