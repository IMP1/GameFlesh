package scn;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import lib.Camera;

import cls.Level;
import cls.object.ObjectWithMass;
import cls.object.Projectile;
import cls.object.ItemDrop;
import cls.trap.BoulderTrap.Boulder;
import cls.trap.Trap;
import cls.enemy.Enemy;
import cls.Player;

public class Map extends Scene {
	
	private Level level;
	private Camera camera;
	private Player[] players;
	private ArrayList<Projectile> projectiles;
	private ArrayList<ItemDrop> itemDrops;
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
		itemDrops = new ArrayList<ItemDrop>();
		visible = new boolean[level.height][level.width];
		visited = new boolean[level.height][level.width];
		update(0);
	}

	@Override
	public void update(double dt) {
		for (int j = 0; j < visible.length; j ++) {
			for (int i = 0; i < visible[j].length; i ++) {
				visible[j][i] = false;
			}
		}
		updatePlayers(dt);
		for (int j = 0; j < visible.length; j ++) {
			for (int i = 0; i < visible[j].length; i ++) {
				if (visible[j][i]) visited[j][i] = true;
			}
		}
		updateEnemies(dt);
		updateCamera();
		updateTraps(dt);
		updateProjectiles(dt);
	}
	
	private void updatePlayers(double dt) {
		for (Player p : players) {
			p.update(dt);
			p.updateVisibilty(this, visible, true);
		}
	}
	
	private void updateEnemies(double dt) {
//		for (Enemy e : level.enemies) {
//			e.update(dt);
//		}
	}
	
	private void updateCamera() {
		double x = 0, y = 0;
		double minX = -1, minY = -1, maxX = -1, maxY = -1;
		for (Player p : players) {
			x += p.getPixelX();
			y += p.getPixelY();
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
		// TODO: Do this when multiplayer works. @multiplayer
		getMinZoom();
	}
	
	private double getMinZoom() {
		double ratioWidth  = (double)(level.width  * Level.TILE_SIZE) / jog.Window.getWidth();
		double ratioHeight = (double)(level.height * Level.TILE_SIZE) / jog.Window.getHeight();
		return Math.max(ratioWidth, ratioHeight);
	}
	
	private void updateTraps(double dt) {
		for (Trap t : level.traps) {
			t.update(dt, this);
		}
	}
	
	public ObjectWithMass[] getObjectsWithMass() {
		ArrayList<ObjectWithMass> objs = new ArrayList<ObjectWithMass>();
		for (Enemy e : level.enemies) {
			objs.add(e);
		}
		for (Player p : players) {
			objs.add(p);
		}
		for (Projectile p : projectiles) {
			if (p.getClass() == Boulder.class) {
				objs.add((Boulder)p);
			}
		}
		for (ItemDrop d : itemDrops) {
			objs.add(d);
		}
		return objs.toArray(new ObjectWithMass[objs.size()]);
	}
	
	@Override
	public void draw() {
		camera.set();
		drawMap();
		camera.unset();
		drawMiniMap();
	}
	
	private void drawMap() {
		for (int j = 0; j < level.tiles.length; j ++) {
			for (int i = 0; i < level.tiles[j].length; i ++) {
				if (hasVisited(i, j)) {
					jog.Graphics.setColour(level.tiles[j][i].color);
					jog.Graphics.rectangle(true, i * Level.TILE_SIZE, j * Level.TILE_SIZE, Level.TILE_SIZE, Level.TILE_SIZE);
				}
				if (!isTileVisible(i, j) && (!level.getTile(i, j).isWall() && level.getTile(i, j) != Level.Tile.FAKE_WALL1 || !hasVisited(i, j))) {
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
	
	private void drawMiniMap() {
		int margin = 32;
		int padding = 8;
		int w = 3;
		jog.Graphics.push();
		jog.Graphics.translate(jog.Window.getWidth() - (level.width * w + margin), margin);
		jog.Graphics.setColour(32, 32, 32);
		jog.Graphics.rectangle(true, -padding, -padding, level.width * w + padding * 2, level.height * w + padding * 2);
		for (int j = 0; j < level.tiles.length; j ++) {
			for (int i = 0; i < level.tiles[j].length; i ++) {
				if (hasVisited(i, j)) {
					jog.Graphics.setColour(level.tiles[j][i].color);
					jog.Graphics.rectangle(true, i * w, j * w, w, w);
				}
			}
		}
		jog.Graphics.setColour(255, 255, 255);
		for (Player p : players) {
			jog.Graphics.circle(true, w * p.getPixelX() / Level.TILE_SIZE, w * p.getPixelY() / Level.TILE_SIZE, 2);
		}
		jog.Graphics.pop();
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
	
	public boolean isPixelVisible(double x, double y) {
		int i = (int)(x / Level.TILE_SIZE);
		int j = (int)(y / Level.TILE_SIZE);
		return isTileVisible(i, j);
	}
	
	public boolean isTileVisible(int i, int j) {
		return visible[j][i];
	}
	
	public boolean hasVisited(int i, int j) {
		return visited[j][i];
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
			if (hasVisited(p.getMapX(), p.getMapY())) {
				p.draw();
			}
		}
	}
	
	@Override
	public void close() {
		
	}

	@Override
	public void keyPressed(int key) {
		if (key == KeyEvent.VK_BACK_QUOTE) {
			revealAll();
		}
	}

	private void revealAll() {
		for (int j = 0; j < level.height; j ++) {
			for (int i = 0; i < level.width; i ++) {
				visited[j][i] = true;
			}
		}
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
