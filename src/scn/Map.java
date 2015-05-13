package scn;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import lib.Camera;

import cls.Level;
import cls.Projectile;
import cls.trap.Trap;
import cls.enemy.Enemy;
import cls.Player;

public class Map extends Scene {
	
	private Level level;
	private Camera camera;
	private Player[] players;
	private ArrayList<Projectile> projectiles;
	private boolean[][] visible;
	private boolean[][] visited;

	public Map(Level level) {
		this.level = level;
	}

	@Override
	public void start() {
		jog.Graphics.setBackgroundColour(64, 64, 64);
		camera = new Camera();
		camera = new Camera(0, 0, level.width * Level.TILE_SIZE, level.height * Level.TILE_SIZE);
		players = new Player[1];
		players[0] = new Player(level.startX, level.startY);
		projectiles = new ArrayList<Projectile>();
		visible = new boolean[level.height][level.width];
		visited = new boolean[level.height][level.width];
		updateCamera();
		updateVisibility();
	}

	@Override
	public void update(double dt) {
		boolean movement = false;
		for (Player p : players) {
			movement = movement || updatePlayerMovement(p, dt);
		}
		if (movement) {
			updateCamera();
			updateVisibility();
		}
		updateProjectiles(dt);
	}
	
	private boolean updatePlayerMovement(Player p, double dt) {
		double dx = 0;
		double dy = 0;
		if (jog.Input.isKeyDown(KeyEvent.VK_W)) {
			dy -= dt * 128;
		}
		if (jog.Input.isKeyDown(KeyEvent.VK_A)) {
			dx -= dt * 128;
		}
		if (jog.Input.isKeyDown(KeyEvent.VK_S)) {
			dy += dt * 128;
		}
		if (jog.Input.isKeyDown(KeyEvent.VK_D)) {
			dx += dt * 128;
		}
		if (dx != 0 || dy != 0) {
			p.move(dx, dy);
			return true;
		}
		return false;
	}

	private void updateVisibility() {
		for (int j = 0; j < visible.length; j ++) {
			for (int i = 0; i < visible[j].length; i ++) {
				visible[j][i] = false;
			}
		}
		for (Player p : players) {
			updateVisibility(p);
		}
		for (int j = 0; j < visible.length; j ++) {
			for (int i = 0; i < visible[j].length; i ++) {
				if (visible[j][i]) visited[j][i] = true;
			}
		}
	}
	
	private void updateVisibility(Player player) {
		int r = player.getSightRadius() * Level.TILE_SIZE;
		int px = (int)player.getX();
		int py = (int)player.getY();
		for (int theta = 0; theta < 360; theta ++) {
			double a = Math.PI * theta / 180;
			raycastAngle(px, py, a + 0.0 * Math.PI, r);
		}
	}
	
	private void raycastAngle(int ox, int oy, double angle, int maxRadius) {
		for (int r = 0; r < maxRadius; r ++) {
			int x = ox + (int)(Math.cos(angle) * r);
			int y = oy + (int)(Math.sin(angle) * r);
			int i = x / Level.TILE_SIZE;
			int j = y / Level.TILE_SIZE;
			if (isTileOpaque(i, j)) {
				if (isInMap(i, j)) visible[j][i] = true;
				return;
			} else {
				visible[j][i] = true;
			}
		}
	}
	
	private void updateCamera() {
		double x = 0, y = 0;
		double minX = -1, minY = -1, maxX = -1, maxY = -1;
		for (Player p : players) {
			x += p.getX();
			y += p.getY();
			if (minX == -1 || x < minX) minX = x;
			if (minY == -1 || y < minY) minY = y;
			if (maxX == -1 || x > maxX) maxX = x;
			if (maxY == -1 || y > maxY) maxY = y;
		}
		x /= players.length;
		y /= players.length;
		camera.centreOn(x, y);
		if (players.length > 1) {
			updateCameraZoom(maxX - minX, maxY - minY);
		}
	}
	
	private void updateCameraZoom(double xDifference, double yDifference) {
		// TODO: Do this when multiplayer works.
		getMinZoom();
	}
	
	private double getMinZoom() {
		double ratioWidth  = (double)(level.width  * Level.TILE_SIZE) / jog.Window.getWidth();
		double ratioHeight = (double)(level.height * Level.TILE_SIZE) / jog.Window.getHeight();
		return Math.max(ratioWidth, ratioHeight);
	}
	
	@Override
	public void draw() {
		camera.set();
		drawMap();
		camera.unset();
	}
	
	private void drawMap() {
		for (int j = 0; j < level.tiles.length; j ++) {
			for (int i = 0; i < level.tiles[j].length; i ++) {
				if (hasVisited(i, j)) {
					jog.Graphics.setColour(level.tiles[j][i].color);
					jog.Graphics.rectangle(true, i * Level.TILE_SIZE, j * Level.TILE_SIZE, Level.TILE_SIZE, Level.TILE_SIZE);
				}
				if (!isVisible(i, j)) {
					jog.Graphics.setColour(0, 0, 32, 96);
					jog.Graphics.rectangle(true, i * Level.TILE_SIZE, j * Level.TILE_SIZE, Level.TILE_SIZE, Level.TILE_SIZE);
				}
			}
		}
		// ---
		jog.Graphics.setColour(255, 255, 255);
		jog.Graphics.printCentred("S", level.startX * Level.TILE_SIZE, level.startY * Level.TILE_SIZE, Level.TILE_SIZE);
		jog.Graphics.printCentred("E", level.endX * Level.TILE_SIZE, level.endY * Level.TILE_SIZE, Level.TILE_SIZE);
		// ---
		for (Trap t : level.traps) {
			t.draw();
		}
		for (Enemy e : level.enemies) {
			e.draw();
		}
		for (Player p : players) {
			p.draw();
		}
		drawProjectiles();
	}
	
	public boolean isInMap(int i, int j) {
		return i >= 0 && i < level.width && j >= 0 && j < level.height;
	}
	
	public boolean isPixelPassable(double x, double y) {
		int i = (int)(x / Level.TILE_SIZE);
		int j = (int)(y / Level.TILE_SIZE);
		return isTilePassable(i, j);
	}
	
	public boolean isTilePassable(int i, int j) {
		return isInMap(i, j) && (level.getTile(i, j).isFloor() || level.isExit(i, j));
	}
	
	public boolean isTileOpaque(int i, int j) { 
		return !isTilePassable(i, j);
	}
	
	public boolean isVisible(int i, int j) {
		return visible[j][i];
	}
	
	public boolean hasVisited(int i, int j) {
		return true;
//		return visited[j][i];
	}
	
	public void addProjectile(Projectile p) {
		projectiles.add(p);
	}
	
	private void updateProjectiles(double dt) {
		for (Projectile p : projectiles) {
			p.update(dt, this);
		}
		for (int i = projectiles.size() - 1; i >= 0; i --) {
			if (projectiles.get(i).isFinished()) {
				projectiles.remove(i);
			}
		}
	}

	private void drawProjectiles() {
		for (Projectile p : projectiles) {
			p.draw();
		}
	}
	
	@Override
	public void close() {
		
	}

	@Override
	public void keyPressed(int key) {
		
	}

	@Override
	public void keyReleased(int key) {}

	@Override
	public void mousePressed(int mouseX, int mouseY, int mouseKey) {
		int mx = (mouseX + (int)camera.getX()) / Level.TILE_SIZE;
		int my = (mouseY + (int)camera.getY()) / Level.TILE_SIZE;
		if (mx >= 0 && mx < level.width && my >= 0 && my < level.height) {
			for (Trap t : level.traps) {
				if (t.isAt(mx, my)) {
					t.trigger(this);
				}
			}
		}
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseKey) {}

	@Override
	public void mouseScrolled(int x, int y, int scroll) {}

	@Override
	public void focus(boolean gained) {}

	@Override
	public void mouseFocus(boolean gained) {}

	@Override
	public void resize(int oldWidth, int oldHeight) {}

	@Override
	public void mouseMoved(int x, int y) {}

	@Override
	public boolean quit() {
		return false;
	}

}
