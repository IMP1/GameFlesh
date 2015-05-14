package cls;

import java.awt.Color;
import cls.enemy.Enemy;
import cls.trap.Trap;

public class Level {
	
	public final static int TILE_SIZE = 48;
	
	public enum Tile {
		NONE(0, 0, 0),
		FLOOR1(128, 128, 128), // Normal Floor
		FLOOR2(128, run.Main.DEBUGGING ? 160 : 128, 128), // Starting Room
		FLOOR3(run.Main.DEBUGGING ? 160 : 128, 128, 128), // Ending Room
		FLOOR4(128, 128, run.Main.DEBUGGING ? 160 : 128), // Boss Room
		
		WALL1(64, 64, 64),
		FAKE_WALL1(64, 64, run.Main.DEBUGGING ? 128 : 64),
		;
		public final Color color;
		Tile(int r, int g, int b) {
			color = new Color(r, g, b);
		}
		public boolean isFloor() {
			return (this == FLOOR1 || this == FLOOR2 || this == FLOOR3 || this == FLOOR4);
		}
		public boolean isWall() {
			return (this == WALL1);
		}
	}
	
	public final int width;
	public final int height;
	public final Tile[][] tiles;
	public final Enemy[] enemies;
	public final Trap[] traps;
	public final int startX, startY;
	public final int endX, endY;
	
	Level(Tile[][] tiles, Trap[] traps, Enemy[] enemies, int startX, int startY, int endX, int endY) {
		this.tiles = tiles;
		this.traps = traps;
		this.enemies = enemies;
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		width = tiles[0].length;
		height = tiles.length;
	}
	
	public Tile getTile(int x, int y) {
		if (y >= 0 && y < height && x >= 0 && x < width) {
			return tiles[y][x];
		} else {
			return Tile.NONE;
		}
	}
	
	public boolean isExit(int x, int y) {
		if (x == startX && y == startY) return true;
		if (x == endX && y == endY) return true;
		return false;
	}

}
