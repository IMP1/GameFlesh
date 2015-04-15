package cls.trap;

public class SwingingTrap extends Trap {
	
	public enum Direction {
		HORIZONTAL,
		VERTICAL;
	}
	
	public final Direction direction;

	public SwingingTrap(int x, int y, Direction direction) {
		super(x, y, "Swinging Trap");
		this.direction = direction;
	}

}
