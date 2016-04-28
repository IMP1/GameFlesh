package cls.level;

import java.awt.Rectangle;
import java.util.ArrayList;

import cls.object.FakeWall;
import cls.trap.*;
import cls.enemy.*;
import cls.level.Level.Tile;

public class LevelGenerator {

	private static class GenerationThread extends Thread {
		@Override
		public void run() {
			runMapGeneration();
		}
	}

	public final static int MIN_WIDTH  = 24;
	public final static int MAX_WIDTH  = 32;
	public final static int MIN_HEIGHT = 24;
	public final static int MAX_HEIGHT = 32;
	
	// Map Generator variables
	private static String message = "Inactive";
	private static boolean isFinished = false;
	private static Level generatedLevel = null;
	private static GenerationThread thread;
	private static ArrayList<Rectangle> createdRooms;
	private static ArrayList<Rectangle> hiddenRooms;
	
	// Map variables
	private static int mapWidth;
	private static int mapHeight;
	private static Tile[][] tiles;
	private static int[][] autoTiles;
	private static ArrayList<Trap> traps;
	private static ArrayList<FakeWall> fakeWalls;
	private static ArrayList<Enemy> enemies;
	private static int startX;
	private static int startY;
	private static int endX;
	private static int endY;
	private static int bossX;
	private static int bossY;
	
	public static void generateMap() {
		isFinished = false;
		createdRooms = new ArrayList<Rectangle>();
		hiddenRooms = new ArrayList<Rectangle>();
		thread = new GenerationThread();
		thread.start();
	}

	public static String getMessage() {
		return message;
	}
	
	public static boolean isFinished() {
		return isFinished;
	}
	
	public static Level getGeneratedMap() {
		return generatedLevel;
	}

	private static void runMapGeneration() {
		initialise();
		createLayout();
		cleanUp();
		addContent();
		generatedLevel = new Level(tiles, autoTiles, traps.toArray(new Trap[traps.size()]), fakeWalls, enemies, startX, startY, endX, endY);
		isFinished = true;
	}
	
	private static void initialise() {
		mapWidth = (int)(Math.random() * (MAX_WIDTH - MIN_WIDTH)) + MIN_WIDTH;
		mapHeight = (int)(Math.random() * (MAX_HEIGHT - MIN_HEIGHT)) + MIN_HEIGHT;
		tiles = new Tile[mapHeight][mapWidth];
		for (int j = 0; j < mapHeight; j ++) {
			for (int i = 0; i < mapWidth; i ++) {
				tiles[j][i] = Tile.NONE;
			}
		}
		traps = new ArrayList<Trap>();
		fakeWalls = new ArrayList<FakeWall>();
		enemies = new ArrayList<Enemy>();
	}
	
	private static void createLayout() {
		createRooms();
		createCorridors();
		joinRooms();
		fillUp();
		hideRooms();
		createColumns();
	}
	
	private static void addContent() {
		addTraps();
		addEnemies();
		addTreasure();
	}
	
	private static void cleanUp() {
		ensureWallThickness();
		generateAutotiles();
	}

	private static void createRooms() {
		LevelGenerator.message = "Creating rooms...";
		createStartingRoom();
		createEndingRoom();
		createBossRoom();
		int largeRooms = (int)(Math.random() * 4) + 3;
		createMiscRooms(largeRooms, 4, 3, 8, 7);
		int mediumRooms = (int)(Math.random() * 4) + 5;
		createMiscRooms(mediumRooms, 3, 2, 6, 5);
		int smallRooms = (int)(Math.random() * 4) + 7;
		createMiscRooms(smallRooms, 2, 2, 4, 4);
	}
		
	private static void createStartingRoom() {
		Rectangle r = addRandomRoom(4, 3, Tile.FLOOR2);
		if (r == null) {
			createStartingRoom();
		} else {
			startX = r.x + (int)(Math.random() * r.width);
			startY = r.y - 1;
		}
	}
	
	private static void createEndingRoom() {
		Rectangle r = addRandomRoom(4, 3, Tile.FLOOR3);
		if (r == null) {
			createEndingRoom();
		} else {
			endX = r.x + (int)(Math.random() * r.width);
			endY = r.y - 1;
		}
	}
	
	private static void createBossRoom() {
		Rectangle r = addRandomRoom(6, 5, Tile.FLOOR4);
		if (r == null) {
			createBossRoom();
		} else {
			bossX = r.x + (int)(Math.random() * r.width);
			bossY = r.y + (int)(Math.random() * r.height);
		}
	}
	
	private static void createMiscRooms(int number, int minWidth, int minHeight, int maxWidth, int maxHeight) {
		for (int i = 0; i < number; i ++) {
			int w = (int)(Math.random() * (maxWidth - minWidth)) + minWidth;
			int h = (int)(Math.random() * (maxHeight - minHeight)) + minHeight;
			addRandomRoom(w, h);
		}
	}
	
	private static Rectangle addRandomRoom(int width, int height) {
		if (Math.random() < 0.1) {
			return addRandomRoom(width, height, Tile.WALL_TOP);
		} else {
			return addRandomRoom(width, height, Tile.FLOOR1);
		}
	}
	private static Rectangle addRandomRoom(int width, int height, Tile tileType) {
		int n = (int)(Math.random() * mapWidth * mapHeight);
		for (int i = n; i < (mapWidth * mapHeight); i ++) {
			int x = i % mapWidth;
			int y = i / mapWidth;
			if (isSpaceAt(x, y, width, height)) {
				return addRoom(x, y, width, height, tileType);
			}
		}
		for (int i = n; i > 0; i --) {
			int x = i % mapWidth;
			int y = i / mapWidth;
			if (isSpaceAt(x, y, width, height)) {
				return addRoom(x, y, width, height, tileType);
			}
		}
		return null;
	}
		
	private static boolean isSpaceAt(int x, int y, int width, int height) {
		return isInMap(x, y, width, height) && !overlapsExistingRoom(x, y, width, height);
	}
	
	private static boolean isInMap(int x, int y, int width, int height) {
		if (x < 1 || x >= mapWidth - width) return false;
		if (y < 1 || y >= mapHeight - height) return false;
		return true;
	}
	
	private static boolean overlapsExistingRoom(int x, int y, int width, int height) {
		for (Rectangle r : createdRooms) {
			boolean horizontalOverlap = false;
			boolean verticalOverlap = false;
			if (r.x < x) {
				if (r.x + r.width + 1 >= x) horizontalOverlap = true;
			} else {
				if (x + width + 1 >= r.x) horizontalOverlap = true;
			}
			if (r.y < y) {
				if (r.y + r.height + 1 >= y) verticalOverlap = true;
			} else {
				if (y + height + 1 >= r.y) verticalOverlap = true;
			}
			if (horizontalOverlap && verticalOverlap) {
				return true;
			}
		}
		return false;
	}
	
	private static Rectangle addRoom(int x, int y, int width, int height, Tile floorType) {
		return addRoom(x, y, width, height, floorType, Tile.WALL_TOP);
	}
	private static Rectangle addRoom(int x, int y, int width, int height, Tile floorType, Tile wallType) {
		if (wallType != null) {
			for (int j = y - 1; j <= y + height; j ++) {
				for (int i = x - 1; i <= x + width; i ++) {
					setTile(i, j, wallType);
				}
			}
		}
		for (int j = y; j < y + height; j ++) {
			for (int i = x; i < x + width; i ++) {
				setTile(i, j, floorType);
			}
		}
		Rectangle r = new Rectangle(x, y, width, height);
		createdRooms.add(r);
		return r;
	}
		
	private static void createCorridors() {
		createMiscRooms(3, 6, 1, 9, 1);
		createMiscRooms(3, 1, 6, 1, 9);
		createMiscRooms(4, 4, 1, 7, 1);
		createMiscRooms(4, 1, 4, 1, 7);
	}
		
	private static void joinRooms() {
		LevelGenerator.message = "Joining rooms...";
		createDoors();
		createPassages();
	}
	
	private static void createDoors() {
		for (int j = 0; j < mapHeight; j ++) {
			for (int i = 0; i < mapWidth; i ++) {
				boolean wallSquare = (
					getTile(i, j).isWall() && !isExit(i, j) &&
					getTile(i + 1, j).isWall() && !isExit(i + 1, j) &&
					getTile(i, j + 1).isWall() && !isExit(i, j + 1) &&
					getTile(i + 1, j + 1).isWall() && !isExit(i + 1, j + 1)
				);
				boolean passageHorizontal = (
					getTile(i - 1, j).isFloor() && getTile(i + 2, j).isFloor() &&
					getTile(i - 1, j + 1).isFloor() && getTile(i + 2, j + 1).isFloor()
				);
				boolean passageVertical = (
					getTile(i, j - 1).isFloor() && getTile(i, j + 2).isFloor() &&
					getTile(i + 1, j - 1).isFloor() && getTile(i + 1, j + 2).isFloor()
				);
				if (wallSquare && (passageHorizontal || passageVertical)) {
					setTile(i, j, Tile.FLOOR1);
					setTile(i + 1, j, Tile.FLOOR1);
					setTile(i, j + 1, Tile.FLOOR1);
					setTile(i + 1, j + 1, Tile.FLOOR1);
				}
			}
		}
		for (int j = 0; j < mapHeight; j ++) {
			for (int i = 0; i < mapWidth; i ++) {
				boolean passageHorizontal = (
					getTile(i, j).isWall() && getTile(i, j - 1).isWall() && getTile(i, j + 1).isWall() &&
					getTile(i + 1, j).isWall() && getTile(i + 1, j - 1).isWall() && getTile(i + 1, j + 1).isWall() &&
					getTile(i - 1, j).isFloor() && getTile(i + 2, j).isFloor() && (
						(getTile(i - 1, j - 1).isWall() && getTile(i - 1, j + 1).isWall()) ||
						(getTile(i + 2, j - 1).isWall() && getTile(i + 2, j + 1).isWall()) ||
						(getTile(i - 1, j - 1).isWall() && getTile(i + 2, j + 1).isWall()) ||
						(getTile(i + 2, j - 1).isWall() && getTile(i - 1, j + 1).isWall())
					)
				);
				if (passageHorizontal) {
					setTile(i, j, Tile.FLOOR1);
					setTile(i + 1, j, Tile.FLOOR1);
				}
			}
		}
		for (int j = 0; j < mapHeight; j ++) {
			for (int i = 0; i < mapWidth; i ++) {
				boolean passageVertical = (
					getTile(i, j).isWall() && getTile(i - 1, j).isWall() && getTile(i + 1, j).isWall() &&
					getTile(i, j + 1).isWall() && getTile(i - 1, j + 1).isWall() && getTile(i + 1, j + 1).isWall() &&
					getTile(i, j - 1).isFloor() && getTile(i, j + 2).isFloor() && (
						(getTile(i - 1, j - 1).isWall() && getTile(i + 1, j - 1).isWall()) ||
						(getTile(i - 1, j + 2).isWall() && getTile(i + 1, j + 2).isWall()) ||
						(getTile(i - 1, j - 1).isWall() && getTile(i + 1, j + 2).isWall()) ||
						(getTile(i - 1, j + 2).isWall() && getTile(i + 1, j - 1).isWall())
					)
				);
				if (passageVertical) {
					setTile(i, j, Tile.FLOOR1);
					setTile(i, j + 1, Tile.FLOOR1);
				}
			}
		}
	}
	
	private static void createPassages() {
		for (int j = 0; j < mapHeight; j ++) {
			for (int i = 0; i < mapWidth; i ++) {
				if (getTile(i, j).isWall()) {
					tryPassage(i, j, 1, 0);
					tryPassage(i, j, 0, 1);
					tryPassage(i, j, -1, 0);
					tryPassage(i, j, 0, -1);
				}
			}
		}
	}
	
	private static void tryPassage(int x, int y, int dx, int dy) {
		boolean validStart = getTile(x - dx, y - dy).isFloor();
		if (!validStart) return;
		int passageLength = checkForPassage(x, y, dx, dy);
		if (passageLength > 0) {
			int width = 1, height = 1;
			int passageWidth = getPassageWidth(x, y, dx, dy);
			if (dx != 0) {
				height = passageWidth;
			} else if (dy != 0) {
				width = passageWidth;
			}
			makePassage(x, y, dx, dy, width, height);
		}
	}
	
	private static int getPassageWidth(int x, int y, int dx, int dy) {
		int width = 1;
		int i = x + dy;
		int j = y + dx;
		while (checkForPassage(i, j, dx, dy) > 0) {
			width ++;
			i += dy;
			j += dx;
		}
		return width;
	}
	
	private static int checkForPassage(int x, int y, int dx, int dy) {
		return checkForPassage(x, y, dx, dy, 0);
	}
	private static int checkForPassage(int x, int y, int dx, int dy, int length) {
		if (x < 0 || x >= mapWidth) return -1;
		if (y < 0 || y >= mapWidth) return -1;
		if (isExit(x + dx, y + dy)) return -1;
		if (getTile(x + dx, y + dy).isWall() && getTile(x + dx + dx, y + dy + dy).isFloor()) {
			return length;
		}
		if (getTile(x + dx, y + dy) == Tile.NONE) return checkForPassage(x + dx, y + dy, dx, dy, length + 1);
		return -1;
	}
	
	private static void makePassage(int x, int y, int dx, int dy, int width, int height) {
		Rectangle r = new Rectangle(x, y, 0, 0);
		setPassageRectangle(x, y, width, height);
		do {
			x += dx;
			y += dy;
			setPassageRectangle(x, y, width, height);
		} while (getTile(x + dx, y + dy) == Tile.NONE);
		x += dx;
		y += dy;
		setPassageRectangle(x, y, width, height);
		r.setSize(x - r.x + 1, y - r.y + 1);
		createdRooms.add(r);
	}
	
	private static void setPassageRectangle(int x, int y, int width, int height) {
		for (int j = y; j < y + height; j ++) {
			for (int i = x; i < x + width; i ++) {
				setTile(i, j, Tile.FLOOR1);
			}
		}
	}
		
	private static void createColumns() {
		for (int j = 0; j < mapHeight; j ++) {
			for (int i = 0; i < mapWidth; i ++) {
				boolean bigFloor = (
					getTile(i - 1, j - 2).isFloor() &&
					getTile(i,     j - 2).isFloor() &&
					getTile(i + 1, j - 2).isFloor() &&
					getTile(i - 1, j - 1).isFloor() &&
					getTile(i,     j - 1).isFloor() &&
					getTile(i + 1, j - 1).isFloor() &&
					getTile(i - 1, j).isFloor() &&
					getTile(i,     j).isFloor() &&
					getTile(i + 1, j).isFloor() &&
					getTile(i - 1, j + 1).isFloor() &&
					getTile(i,     j + 1).isFloor() &&
					getTile(i + 1, j + 1).isFloor()
				);
				boolean occupied = (
					isTrapAt(i, j) ||
					isTrapAt(i, j - 1)
				);
				double frequency = 0.1;
				if (bigFloor && !occupied && Math.random() <= frequency) {
					setTile(i, j, Tile.WALL_SIDE);
					setTile(i, j - 1, Tile.WALL_TOP);
				}
			}
		}
	}
		
	private static void fillUp() {
		for (int j = 0; j < mapHeight; j ++) {
			for (int i = 0; i < mapWidth; i ++) {
				if (getTile(i, j) == Tile.NONE) {
					setTile(i, j, Tile.WALL_TOP);
				}
			}
		}
	}
	
	private static void addTraps() {
		LevelGenerator.message = "Adding traps...";
		addBoulderTraps();
		addSpikeTraps();
		addSwingingTraps();
		addArrowTraps();
	}
	
	private static void addBoulderTraps() {
		double likelihood = 0.2;
		for (Rectangle r : createdRooms) {
			if (Math.random() <= likelihood && ((r.width == 1 && r.height > 3) || (r.width > 3 && r.height == 1))) {
				placeBoulderTrap(r);
			} else if (Math.random() <= likelihood / 2) {
				placeBoulderTrap(r);
			}
		}
	}
	
	private static void placeBoulderTrap(Rectangle r) {
		int trapX = r.x;
		int trapY = r.y;
		int boulderX = trapX;
		int boulderY = trapY;
		if (r.height > r.width) {
			while (Math.abs(trapY - boulderY) < Math.min(r.height - 1, 3)) {
				double[] rand = boulderPosition(r.height);
				trapY = r.y + (int)(rand[0]);
				boulderY = r.y + r.height - 1 - (int)(rand[1]);
			}
		} else {
			while (Math.abs(trapX - boulderX) < Math.min(r.width - 1, 3)) {
				double[] rand = boulderPosition(r.width);
				trapX = r.x + (int)(rand[0]);
				boulderX = r.x + r.width - 1 - (int)(rand[1]);
			}
		}
		boolean sameLocation = trapX == boulderX && trapY == boulderY;
		boolean tooNearExit = Math.abs(trapX - startX) + Math.abs(trapY - startY) <= 1 ||
							  Math.abs(trapX - endX)   + Math.abs(trapY - endY)   <= 1; 
		if (getTile(trapX, trapY).isFloor() && !isExit(trapX, trapY) &&
				 !tooNearExit && !sameLocation) {
			traps.add(new BoulderTrap(trapX, trapY, boulderX, boulderY));
		}
	}
	
	private static double[] boulderPosition(int max) {
		int factor = 4;
		if (Math.random() < 0.5) {
			return new double[] {
				lowRandom(Math.random() * max, max, factor),
				lowRandom(Math.random() * max, max, factor)
			};
		} else {
			return new double[] {
				highRandom(Math.random() * max, max, factor),
				highRandom(Math.random() * max, max, factor)
			};
		}
	}
	
	private static double lowRandom(double i, double max, double factor) {
		return Math.pow(i, factor) / Math.pow(max, factor - 1);
	}
	
	private static double highRandom(double i, double max, double factor) {
		return max - Math.pow(i, factor) / Math.pow(max, factor - 1);
	}
	
	private static void addSpikeTraps() {
		double likelihood = 0.01;
		for (int j = 0; j < mapHeight; j ++) { 
			for (int i = 0; i < mapWidth; i ++) {
				if (getTile(i, j).isFloor() && !isExit(i, j) && !isTrapAt(i,j) &&
						Math.abs(i - startX) + Math.abs(j - startY) > 2 &&
						Math.abs(i - endX) + Math.abs(j - endY) > 2 &&
						Math.random() <= likelihood) {
					traps.add(new SpikeTrap(i, j));
				}
			}
		}
	}
	
	private static void addSwingingTraps() {
		double likelihood = 0.3;
		for (int j = 0; j < mapHeight; j ++) {
			for (int i = 0; i < mapWidth; i ++) {
				if (isVerticalPassage(i, j, 1) && !isTrapAt(i, j - 1) && !isTrapAt(i, j + 1) && 
						Math.random() <= likelihood) {
					traps.add(new SwingingTrap(i, j, SwingingTrap.Direction.HORIZONTAL));
				} else if (isHorizontalPassage(i, j, 1) && !isTrapAt(i - 1, j) && !isTrapAt(i + 1, j) &&
						Math.random() <= likelihood) {
					traps.add(new SwingingTrap(i, j, SwingingTrap.Direction.VERTICAL));
				}
			}
		}
	}
	
	private static void addArrowTraps() {
		double likelihood = 0.1;
		for (int j = 0; j < mapHeight; j ++) { 
			for (int i = 0; i < mapWidth; i ++) {
				if (getTile(i, j).isFloor() && !isExit(i, j) && Math.random() <= likelihood) {
					placeArrowTrap(i, j);
				}
			}
		}
	}
	
	private static void placeArrowTrap(int x, int y) {
		int direction = (int)(Math.random() * 4);
		int n = 0;
		switch (direction) {
		case 0: // right
			n = arrowTrapDistance(x, y, 1, 0);
			if (n > 3 && n < 7) {
				traps.add(new ArrowTrap(x, y, x + n, y));
			}
			break;
		case 1: // down
			n = arrowTrapDistance(x, y, 0, 1);
			if (n > 3 && n < 7) {
				traps.add(new ArrowTrap(x, y, x, y + n));
			}
			break;
		case 2: // left
			n = arrowTrapDistance(x, y, -1, 0);
			if (n > 3 && n < 7) {
				traps.add(new ArrowTrap(x, y, x - n, y));
			}
			break;
		case 3: // up
			n = arrowTrapDistance(x, y, 0, -1);
			if (n > 3 && n < 7) {
				traps.add(new ArrowTrap(x, y, x, y - n));
			}
			break;
		}
	}
	
	private static int arrowTrapDistance(int x, int y, int dx, int dy) {
		return arrowTrapDistance(x, y, dx, dy, 0);
	}
	private static int arrowTrapDistance(int x, int y, int dx, int dy, int distance) {
		if (x < 0 || x >= mapWidth) return -1;
		if (y < 0 || y >= mapWidth) return -1;
		if (isExit(x + dx, y + dy)) return -1;
		if (isTrapAt(x + dx, y + dy)) return -1;
		if (getTile(x + dx, y + dy).isWall()) {
			return distance + 1;
		}
		if (getTile(x + dx, y + dy).isFloor()) return arrowTrapDistance(x + dx, y + dy, dx, dy, distance + 1);
		return -1;
	}
	
	private static boolean isHorizontalPassage(int x, int y, int minimumLengthEitherSide) {
		if (!getTile(x, y).isFloor()) return false;
		if (Math.abs(x - startX) + Math.abs(y - startY) < minimumLengthEitherSide + 1) {
			return false;
		}
		for (int i = x - minimumLengthEitherSide; i <= x + minimumLengthEitherSide; i ++) {
			if (!getTile(i, y).isFloor() || !getTile(i, y - 1).isWall() || !getTile(i, y + 1).isWall()) {
				return false;
			}
		}
		return true;
	}

	private static boolean isVerticalPassage(int x, int y, int minimumLengthEitherSide) {
		if (!getTile(x, y).isFloor()) return false;
		if (Math.abs(x - startX) + Math.abs(y - startY) < minimumLengthEitherSide + 1) {
			return false;
		}
		for (int j = y - minimumLengthEitherSide; j <= y + minimumLengthEitherSide; j ++) {
			if (!getTile(x, j).isFloor() || !getTile(x - 1, j).isWall() || !getTile(x + 1, j).isWall())	{
				return false;
			}
		}
		return true;
	}
	
	private static boolean isTrapAt(int x, int y) {
		for (Trap t : traps) {
			if (t.isAt(x, y)) {
				return true;
			}
		}
		return false;
	}
	
	private static void hideRooms() {
		double likelihood = 0.1;
		LevelGenerator.message = "Hiding some rooms...";
		for (Rectangle r : createdRooms) {
			if (Math.random() <= likelihood) {
				hideRectangle(r);
			}
		}
	}
	
	private static void hideRectangle(Rectangle r) {
		hiddenRooms.add(r);
		for (int j = r.y - 1; j < r.y + r.height + 1; j ++) {
			if (getTile(r.x - 1, j).isFloor() && !isExit(r.x - 1, j - 1)) {
				hideWall(r.x - 1, j);
			}
			if (getTile(r.x + r.width, j).isFloor() && !isExit(r.x + r.width, j - 1)) {
				hideWall(r.x + r.width, j);
			}
		}
		for (int i = r.x - 1; i < r.x + r.width + 1; i ++) {
			if (getTile(i, r.y - 1).isFloor() && !isExit(i, r.y - 2)) {
				placeHorizontalHiddenWall(i,  r.y - 1);
			}
			if (getTile(i, r.y + r.height).isFloor() && !isExit(i, r.y + r.height - 1)) {
				placeHorizontalHiddenWall(i,  r.y - 1);
			}
		}
	}
	
	private static void placeHorizontalHiddenWall(int x, int y) {
		if (isExit(x, y) || isExit(x, y - 1)) return;
		hideWall(x, y);
		if (getTile(x - 1, y + 1).isWall(true) || getTile(x + 1, y + 1).isWall(true)) {
			hideWall(x, y + 1);
		} else {
			hideWall(x, y - 1);
		}
	}
	
	private static void hideWall(int x, int y) {
		if (x > 0 && y > 0 && x < mapWidth - 1 && y < mapHeight - 1) {
			setTile(x, y, Tile.FAKE_WALL);
		}
	}
	
	private static void addEnemies() {
		int numberOfEnemies = 12 + (int)(Math.random() * 12);
		for (int i = 0; i < numberOfEnemies; i ++) {
			addRandomEnemy();
		}
		addBossEnemy();
	}
	
	private static void addRandomEnemy() {
		int x = (int)(Math.random() * mapWidth);
		int y = (int)(Math.random() * mapHeight);
		while (!validEnemyPlacement(x, y)) {
			x = (int)(Math.random() * mapWidth);
			y = (int)(Math.random() * mapHeight);
		}
		enemies.add(new SkeletonEnemy(x, y));
	}
	
	private static void addBossEnemy() {
		enemies.add(new SkeletonEnemy(bossX, bossY));
	}
	
	private static void addTreasure() {
		// TODO add treasuse
	}
	
	private static void ensureWallThickness() {
		for (int j = 0; j < mapHeight; j ++) {
			for (int i = 0; i < mapWidth; i ++) {
				if (isExit(i, j)) {
					setTile(i, j - 1, Tile.WALL_TOP);
				} else if (getTile(i, j).isWall() && getTile(i, j - 1).isFloor() && getTile(i, j + 1).isFloor()) {
					if (getTile(i - 1, j - 1).isWall(true) || getTile(i + 1, j - 1).isWall(true)) {
						setTile(i, j - 1, Tile.WALL_TOP);
					} else {
						setTile(i, j + 1, Tile.WALL_SIDE);
					}
				}
			}
		}
	}
	
	private static void generateAutotiles() {
		autoTiles = new int[tiles.length][tiles[0].length];
		for (int j = 0; j < autoTiles.length; j ++) {
			for (int i = 0; i < autoTiles[j].length; i ++) {
				autoTiles[j][i] = getAutotileValue(i, j);
			}
		}
	}
	
	private static int getAutotileValue(int i, int j) {
		if (startX == i && startY == j) return 8;
		if (endX == i && endY == j) return 9;
		if (getTile(i, j).isFloor()) {
			if (Math.random() < 0.02) {
				return 3;
			} else if (Math.random() < 0.02) {
				return 2;
			} else {
				return 1;
			}
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
		} else {
			return 0;
		}
	}
	
	private static boolean validEnemyPlacement(int x, int y) {
		if (!getTile(x, y).isFloor()) return false;
		for (Enemy e : enemies) {
			if (e.isAtPixel(x * Level.TILE_SIZE, y * Level.TILE_SIZE, 16)) {
				return false;
			}
		}
		for (Trap t : traps) {
			if (t.isAt(x, y)) {
				return false;
			}
		}
		int dx = x - startX;
		int dy = y - startY;
		if (dx * dx + dy * dy < 3) {
			return false;
		}
		return true;
	}
	
	private static void setTile(int x, int y, Tile type) {
		setTile(x, y, type, true);
	}
	private static void setTile(int x, int y, Tile type, boolean overwrite) {
		if (isExit(x, y)) return;
		if (y >= 0 && y < mapHeight && x >= 0 && x < mapWidth) {
			if (overwrite || tiles[y][x] == Tile.NONE) {
				if (type == Tile.FAKE_WALL) {
					fakeWalls.add(new FakeWall(x, y));
				}
				tiles[y][x] = type;
			}
		}
	}
	
	private static Tile getTile(int x, int y) {
		if (y >= 0 && y < mapHeight && x >= 0 && x < mapWidth) {
			return tiles[y][x];
		} else {
			return Tile.NONE;
		}
	}
	
	private static boolean isExit(int x, int y) {
		if (x == startX && y == startY) return true;
		if (x == endX && y == endY) return true;
		return false;
	}
	
}
