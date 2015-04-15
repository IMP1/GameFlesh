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
	
	private Level map;
	private Camera camera;
	private Player player;
	private ArrayList<Projectile> projectiles;

	public Map(Level map) {
		this.map = map;
	}

	@Override
	public void start() {
		jog.Graphics.setBackgroundColour(108, 128, 128);
		camera = new Camera(0, 0, map.width * Level.TILE_SIZE, map.height * Level.TILE_SIZE);
		player = new Player(map.startX, map.startY);
		projectiles = new ArrayList<Projectile>();
	}

	@Override
	public void update(double dt) {
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
		player.move(dx, dy);
		camera.centreOn(player.getX(), player.getY());
		updateProjectiles(dt);
	}

	@Override
	public void draw() {
		camera.set();
		drawMap();
		camera.unset();
	}
	
	private void drawMap() {
		for (int j = 0; j < map.tiles.length; j ++) {
			for (int i = 0; i < map.tiles[j].length; i ++) {
				jog.Graphics.setColour(map.tiles[j][i].color);
				jog.Graphics.rectangle(true, i * Level.TILE_SIZE, j * Level.TILE_SIZE, Level.TILE_SIZE, Level.TILE_SIZE);
				jog.Graphics.setColour(255, 255, 255, 96);
				jog.Graphics.rectangle(false, i * Level.TILE_SIZE, j * Level.TILE_SIZE, Level.TILE_SIZE, Level.TILE_SIZE);
			}
		}
		// ---
		jog.Graphics.setColour(255, 255, 255);
		jog.Graphics.printCentred("S", map.startX * Level.TILE_SIZE, map.startY * Level.TILE_SIZE, Level.TILE_SIZE);
		jog.Graphics.printCentred("E", map.endX * Level.TILE_SIZE, map.endY * Level.TILE_SIZE, Level.TILE_SIZE);
		// ---
		for (Trap t : map.traps) {
			t.draw();
		}
		for (Enemy e : map.enemies) {
			e.draw();
		}
		player.draw();
		drawProjectiles();
	}
	
	public boolean isPassable(double x, double y) {
		int mx = (int)(x / Level.TILE_SIZE);
		int my = (int)(y / Level.TILE_SIZE);
		return map.getTile(mx, my).isFloor() || map.isExit(mx, my);
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
		if (mx >= 0 && mx < map.width && my >= 0 && my < map.height) {
			for (Trap t : map.traps) {
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
