package cls.object;

public abstract class ObjectWithMass {
	
	public ObjectWithMass(double x, double y, int r, int m) {
		pixelX = x;
		pixelY = y;
		radius = r;
		mass = m;
	}
	
	protected int mass;
	protected int radius;
	protected double pixelX;
	protected double pixelY;
	
	public int getMass() { 
		return mass;
	}
	
	public boolean isAtTile(int i, int j) {
		return i == getMapX() && j == getMapY();
	}
	
	public boolean isAtPixel(double x, double y, int leeway) {
		double dx = getPixelX() - x;
		double dy = getPixelY() - y;
		int dr = leeway + radius;
		return dx * dx + dy * dy < dr * dr;
	}

	public double getPixelX() {
		return pixelX;
	}
	
	public double getPixelY() {
		return pixelY;
	}
	
	public int getMapX() {
		return (int)(pixelX / cls.level.Level.TILE_SIZE);
	}
	
	public int getMapY() {
		return (int)(pixelY / cls.level.Level.TILE_SIZE);
	}
	
	public double getScreenX() {
		// TODO: This
		return jog.Window.getWidth() / 2;
	}
	
	public double getScreenY() {
		// TODO: This
		return jog.Window.getHeight() / 2;
	}
	
}
