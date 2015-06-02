package cls.object;

import cls.gui.Popup;

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
	
	public void damage(int damage) {
		changeHealth(-damage);
		double x = pixelX + Math.random() * radius - radius / 2;
		Popup p = new Popup(String.valueOf(damage), x, pixelY - radius * 2);
		((scn.Map)scn.SceneManager.scene()).addPopup(p);
	}
	
	protected void changeHealth(int damage) {
		currentHealth = currentHealth + damage;
		if (currentHealth <= 0) {
			destroy();
		}
		if (currentHealth > maxHealth) {
			currentHealth = maxHealth;
		}
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
