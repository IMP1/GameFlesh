package scn;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import lib.gamepad.Gamepad;
import lib.gamepad.GamepadManager;
import lib.gamepad.GamepadManager.GeneralGamepadHandler;

import run.Cache;

import cls.level.LevelGenerator;
import cls.player.GamepadInput;
import cls.player.KeyboardMouseInput;
import cls.player.Player;

public class Title extends Scene implements GeneralGamepadHandler {
	
	private static lib.Animation fire = new lib.Animation(Cache.image("fire.png"), 4, 1, 4, true, 0.1);
	private static jog.Image background = Cache.image("title.png");
	private static jog.Audio.Source bgm = Cache.audio("FromHere.ogg");

	private enum Stage {
		INITIAL_SCROLL,
		CHARACTER_SELECTION,
		LOADING_MAP,
	}
	
	private Stage currentStage;
	private ArrayList<Player> players;

	@Override
	public void start() {
		GamepadManager.addGeneralHandler(this);
		currentStage = Stage.INITIAL_SCROLL;
		players = new ArrayList<Player>();
		bgm.play();
		fire.start();
	}

	@Override
	public void update(double dt) {
		fire.update(dt);
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
		jog.Graphics.printCentred("Press [Space] or [Start] to Begin", jog.Window.getWidth() / 2, jog.Window.getHeight() - 128);
	}

	@Override
	public void close() {
//		bgm.stop();
	}

	@Override
	public void keyPressed(int key) {
		if (currentStage == Stage.INITIAL_SCROLL && key == KeyEvent.VK_SPACE) {
			players.add(new Player(new KeyboardMouseInput()));
			startGame();
		}
	}

	@Override
	public void buttonPressed(Gamepad controller, int button) {
		if (currentStage == Stage.INITIAL_SCROLL && button == 7) {
			System.out.println(button);
			players.add(new Player(new GamepadInput(GamepadManager.getGamepads()[0])));
			startGame();
		}
	}

	private void startGame() {
		currentStage = Stage.LOADING_MAP;
		LevelGenerator.generateMap();
	}

	@Override
	public boolean quit() {
		return false;
	}

	@Override
	public void buttonReleased(Gamepad controller, int button) {
		// 		
	}

}
