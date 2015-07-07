package cls.player;

public abstract class InputHandler {
	
	public void updateInput(Player player, double dt) {
		updateMovement(player, dt);
		updateDirection(player);
	}

	protected abstract void updateMovement(Player player, double dt);
	protected abstract void updateDirection(Player player);
	
}
