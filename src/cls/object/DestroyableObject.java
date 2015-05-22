package cls.object;

public abstract class DestroyableObject extends ObjectWithMass {

	protected int maxHealth;
	protected int currentHealth;
	protected boolean destroyed;
	
	/**
	 * Reduces the objects health.
	 * @param damage the amount of damage the attacker is giving out.
	 * @return the amount of damage that was actually dealt.
	 */
	public int damage(int damage) {
		currentHealth = currentHealth - damage;
		if (currentHealth <= 0) {
			destroyed = true;
			currentHealth = 0;
		}
		return damage;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
}
