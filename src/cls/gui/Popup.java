package cls.gui;

import java.awt.Color;

public class Popup {
	
	public enum Type {
		DEFAULT(Color.WHITE),
		;
		public final Color colour;
		private Type(Color colour) {
			this.colour = colour;
		}
	}
	
	private String message;
	private Type type;
	private double duration;
	private double timer;
	private double pixelX;
	private double pixelY;
	private double offsetY;
	private boolean finished;
	
	public Popup(String message, double x, double y) {
		this(message, x,  y, Type.DEFAULT);
	}
	public Popup(String message, double x, double y, Type type) {
		this(message, x,  y, type, 1);
	}
	public Popup(String message, double x, double y, Type type, double duration) {
		this.message = message;
		this.type = type;
		this.duration = duration;
		this.pixelX = x;
		this.pixelY = y;
		this.offsetY = 0;
		this.timer = 0;
		this.finished = false;
	}
	
	public void update(double dt) {
		timer += dt;
		if (timer >= duration) {
			finished = true;
		}
		offsetY = -lib.maths.Easing.cubicOut(timer, 16, duration);
	}
	
	public void draw() {
		jog.Graphics.setColour(type.colour);
		jog.Graphics.printCentred(message, pixelX, pixelY + offsetY);
	}
	
	public boolean isFinished() {
		return finished;
	}

}
