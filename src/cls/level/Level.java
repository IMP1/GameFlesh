package cls.level;

import java.awt.Color;
import java.util.ArrayList;

import run.Cache;

import cls.enemy.Enemy;
import cls.object.FakeWall;
import cls.trap.Trap;

public class Level {
	
	public final static Color minimapStartColour = new Color(160, 192, 160);
	public final static Color minimapEndColour   = new Color(192, 160, 160);
	
	public final static jog.Image tileImage = Cache.image("tiles.png");
	public final static int IMAGE_TILE_SIZE = 16;
	public final static int TILE_SIZE = 48;
	
	public enum Tile {
		NONE(0, 0, 0),
		FLOOR1(128, 128, 128), // Normal Floor
		FLOOR2(128, run.Main.DEBUGGING ? 160 : 128, 128), // Starting Room
		FLOOR3(run.Main.DEBUGGING ? 160 : 128, 128, 128), // Ending Room
		FLOOR4(128, 128, run.Main.DEBUGGING ? 160 : 128), // Boss Room
		
		WALL_TOP(64, 64, 64),
		WALL_SIDE(64, 64, 64),
		FAKE_WALL1(64, 64, run.Main.DEBUGGING ? 128 : 64),
		;
		public final Color color;
		Tile(int r, int g, int b) {
			color = new Color(r, g, b);
		}
		public boolean isFloor() {
			return (this == FLOOR1 || this == FLOOR2 || this == FLOOR3 || this == FLOOR4);
		}
		public boolean isWall(boolean includeFakeWalls) {
			return (this == WALL_TOP || this == WALL_SIDE || (includeFakeWalls && this == FAKE_WALL1));
		}
		public boolean isWall() {
			return isWall(false);
		}
	}
	
	public final int width;
	public final int height;
	public final Tile[][] tiles;
	public final int[][] autoTiles;
	public final ArrayList<Enemy> enemies;
	public final Trap[] traps;
	public final ArrayList<FakeWall> fakeWalls;
	public final int startX, startY;
	public final int endX, endY;
	
	Level(Tile[][] tiles, int[][] autoTiles, Trap[] traps, ArrayList<FakeWall> fakeWalls, ArrayList<Enemy> enemies, int startX, int startY, int endX, int endY) {
		this.tiles = tiles;
		this.autoTiles = autoTiles;
		this.traps = traps;
		this.enemies = enemies;
		this.fakeWalls = fakeWalls;
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
	
	public void setTile(int x, int y, Tile newTile) {
		tiles[y][x] = newTile;
		updateAutotiles(x, y);
	}

	private void updateAutotiles(int x, int y) {
		for (int j = y - 2; j <= y + 2; j ++) {
			if (j < 0 || j >= height) continue;
			for (int i = x - 1; i <= x + 1; i ++) {
				if (i < 0 || i >= width) continue;
				autoTiles[j][i] = getAutotileValue(i, j);
			}
		}
	}
	
	private int getAutotileValue(int i, int j) {
		if (getTile(i, j).isFloor()) {
			return 1;
		} else if (getTile(i, j).isWall(true)) {
			int binaryFlagSum = 0;
			if (getTile(i, j + 1).isFloor()) {
				if (getTile(i + 1, j).isWall()) binaryFlagSum += 1;
				if (getTile(i - 1, j).isWall()) binaryFlagSum += 2;
				return binaryFlagSum + 4;
			} else {
				if (getTile(i, j - 1).isFloor()) binaryFlagSum += 1;
				if (getTile(i + 1, j).isFloor() || (getTile(i + 1, j).isWall(true) && getTile(i + 1, j + 1).isFloor())) binaryFlagSum += 2;
				if (getTile(i, j + 2).isFloor() && getTile(i, j + 1).isWall(true)) binaryFlagSum += 4;
				if (getTile(i - 1, j).isFloor() || (getTile(i - 1, j).isWall(true) && getTile(i - 1, j + 1).isFloor())) binaryFlagSum += 8;
				return binaryFlagSum + 32;
			}
		}
		return autoTiles[j][i];
	}
	

}
