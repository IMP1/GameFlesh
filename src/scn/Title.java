package scn;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import lib.gamepad.GamepadManager;

import run.Cache;

import cls.level.LevelGenerator;
import cls.player.GamepadInput;
import cls.player.KeyboardMouseInput;
import cls.player.Player;

public class Title extends Scene {
	
	private static lib.Animation fire = new lib.Animation(Cache.image("fire.png"), 4, 1, 4, true, 0.1);
	private static jog.Image background = Cache.image("title.png");

	private enum Stage {
		INITIAL_SCROLL,
		CHARACTER_SELECTION,
		LOADING_MAP,
	}
	
	private Stage currentStage;
	private ArrayList<Player> players;

	@Override
	public void start() {
		currentStage = Stage.INITIAL_SCROLL;
		players = new ArrayList<Player>();
		fire.start();
		if (run.Main.DEBUGGING) {
//			startGame();
		}
	}

	@Override
	public void update(double dt) {
		fire.update(dt);
		if (currentStage == Stage.INITIAL_SCROLL) {
			if (jog.Input.isKeyDown(KeyEvent.VK_SPACE)) {
//				players.add(new Player(new KeyboardMouseInput()));
				players.add(new Player(new GamepadInput(GamepadManager.getGamepads()[0])));
				startGame();
			}
		}
		if (currentStage == Stage.LOADING_MAP && LevelGenerator.isFinished()) {
			SceneManager.setScene(new Map(LevelGenerator.getGeneratedMap(), players.toArray(new Player[players.size()])));
			return;
		}
	}

	@Override
	public void draw() {
		jog.Graphics.draw(background, 0, 0, 4);
		if (currentStage == Stage.LOADING_MAP) {
			jog.Graphics.printCentred(LevelGenerator.getMessage(), jog.Window.getWidth() / 2, 128);
		}
		jog.Graphics.draw(fire, 544, 320, 1.5);
		jog.Graphics.printCentred("Press [Space] to Begin", jog.Window.getWidth() / 2, jog.Window.getHeight() - 128);
	}

	@Override
	public void close() {
		
	}

	@Override
	public void keyPressed(int key) {
		
	}
	
	private void startGame() {
		currentStage = Stage.LOADING_MAP;
		LevelGenerator.generateMap();
	}

	@Override
	public void keyReleased(int key) {
		
	}

	@Override
	public void mousePressed(int mouseX, int mouseY, int mouseKey) {
		
	}

	@Override
	public void mouseReleased(int mouseX, int mouseY, int mouseKey) {
		
	}

	@Override
	public void mouseScrolled(int x, int y, int scroll) {
		
	}

	@Override
	public void focus(boolean gained) {
		
	}

	@Override
	public void mouseFocus(boolean gained) {
		
	}

	@Override
	public void resize(int oldWidth, int oldHeight) {
		
	}

	@Override
	public void mouseMoved(int x, int y) {
		
	}

	@Override
	public boolean quit() {
		return false;
	}

}
