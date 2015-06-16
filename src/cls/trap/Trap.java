package cls.trap;

public abstract class Trap {
	
	protected int triggerX, triggerY;
	protected boolean triggered;
	public final String name;
	protected final int minTriggerableMass;
	protected final int maxTriggerableMass;

	public Trap(int x, int y, String name) {
		this(x, y, name, 1, Integer.MAX_VALUE);
	}
	public Trap(int x, int y, String trapName, int minMass, int maxMass) {
		triggerX = x;
		triggerY = y;
		name = trapName;
		minTriggerableMass = minMass;
		maxTriggerableMass = maxMass;
		triggered = false;
	}
	
	public void update(double dt, scn.Map scene) {
		for (cls.object.ObjectWithMass obj : scene.getObjectsWithMass()) {
			if (obj.isAtTile(triggerX, triggerY) && obj.getMass() >= minTriggerableMass && obj.getMass() <= maxTriggerableMass) {
				trigger(scene);
			}
		}
	}
	
	public void draw() {
		if (((scn.Map)scn.SceneManager.scene()).hasVisited(triggerX, triggerY)) {
			int w = cls.level.Level.TILE_SIZE;
			int h = cls.level.Level.TILE_SIZE;
			jog.Graphics.setColour(64, 64, 64, 64);
			jog.Graphics.rectangle(true, triggerX * w + 4, triggerY * h + 4, w - 8, h - 8);
		}
	}
	
	public boolean isAt(int i, int j) {
		return (triggerX == i && triggerY == j);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public abstract void trigger(scn.Map scene);

}
