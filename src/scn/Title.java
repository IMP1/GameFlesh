package scn;

import java.awt.event.KeyEvent;

public class Title extends Scene {

	private enum Stage {
		INITIAL_SCROLL,
		MENU_FADE_IN,
	}
	
	private Stage currentStage;
	private double timer;
	private double logoPosition;
	private lib.Parallax[] backgrounds;

	@Override
	public void start() {
		timer = 0;
		backgrounds = new lib.Parallax[3];
		backgrounds[0] = new lib.Parallax(new jog.Image("gfx/sky.png"),       0, 0, 0,   true, false);
		backgrounds[1] = new lib.Parallax(new jog.Image("gfx/mountains.png"), 0, 0, 0.1, true, false);
		backgrounds[2] = new lib.Parallax(new jog.Image("gfx/terrain.png"),   0, 0, 0.2, true, false);
		currentStage = Stage.INITIAL_SCROLL;
	}

	@Override
	public void update(double dt) {
		timer += dt;
		if (currentStage == Stage.INITIAL_SCROLL) {
			for (lib.Parallax p : backgrounds) {
				p.scroll(dt * 256, 0);
			}
			if (timer >= 8) {
				currentStage = Stage.MENU_FADE_IN;
			}
		}
		
		logoPosition = lib.Easing.bezierCurve(timer, 128, 2, 0, -0.5, 1.5, 1);
	}

	@Override
	public void draw() {
		jog.Graphics.push();
		jog.Graphics.scale(2, 2);
		for (lib.Parallax p : backgrounds) {
			p.draw();
		}
		jog.Graphics.pop();
		jog.Graphics.printCentred("Mnul", 0, logoPosition + 64, jog.Window.getWidth());
		
		// Draw debug
		jog.Graphics.print(String.format("%.2f", timer), 0, 0);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(int key) {
		
	}

	@Override
	public void keyReleased(int key) {
		// TODO Auto-generated method stub

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
