package cls;

public abstract class ObjectWithMass {
	
	protected int mass;
	protected double pixelX;
	protected double pixelY;
	
	public int getMass() { return mass; }
	
	public boolean isAtTile(int i, int j) {
		return i == getMapX() && j == getMapY();
	}
	
	public boolean isAtPixel(double x, double y, int leeway) {
		double dx = getPixelX() - x;
		double dy = getPixelY() - y;
		int dr = leeway;
		return dx * dx + dy * dy < dr * dr;
	}

	public double getPixelX() {
		return pixelX;
	}
	
	public double getPixelY() {
		return pixelY;
	}
	
	public int getMapX() {
		return (int)(pixelX / Level.TILE_SIZE);
	}
	
	public int getMapY() {
		return (int)(pixelY / Level.TILE_SIZE);
	}	
	
}
