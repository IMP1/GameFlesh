package cls.player;

import lib.Camera;

public abstract class InputHandler {
	
	protected Player player;
	
	public void updateInput(Camera camera, double dt) {
		updateMovement(dt);
		updateDirection(camera);
	}

	protected abstract void updateMovement(double dt);
	protected abstract void updateDirection(Camera camera);
	
}
