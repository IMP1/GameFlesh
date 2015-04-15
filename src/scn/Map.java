package scn;

import java.awt.event.KeyEvent;

import lib.Camera;

import cls.Level;
import cls.trap.Trap;
import cls.enemy.Enemy;

public class Map extends Scene {
	
	private Level map;
	private Camera camera;

	public Map(Level map) {
		this.map = map;
	}

	@Override
	public void start() {
		jog.Graphics.setBackgroundColour(108, 128, 128);
		camera = new Camera(0, 0, map.width * Level.TILE_SIZE, map.height * Level.TILE_SIZE);
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
		camera.move(dx, dy);
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
	public void mousePressed(int mouseX, int mouseY, int mouseKey) {}

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
