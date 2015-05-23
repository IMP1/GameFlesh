package run;

import java.util.HashMap;

public final class Cache {
	private Cache() {}
	
	private static HashMap<String, jog.Image> images = new HashMap<String, jog.Image>();
	
	public static jog.Image loadImage(String path) {
		if (!images.containsKey(path)) {
			 images.put(path, new jog.Image(path));
		}
		return images.get(path);
	}
	
}
