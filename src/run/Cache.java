package run;

import java.util.HashMap;

public class Cache {
	private Cache() {}
	
	private static HashMap<String, jog.Image> images = new HashMap<String, jog.Image>();
	private static HashMap<String, jog.Audio.Source> audio = new HashMap<String, jog.Audio.Source>();
	
	public static jog.Image image(String path) {
		path = "gfx/" + path;
		if (!images.containsKey(path)) {
			images.put(path, new jog.Image(path));
		}
		return images.get(path);
	}
	
	public static jog.Audio.Source audio(String path) {
		path = "sfx/" + path;
		if (!audio.containsKey(path)) {
			audio.put(path, jog.Audio.newSource(path));
		}
		return audio.get(path);
	}
	
}
