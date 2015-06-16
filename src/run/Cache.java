package run;

import java.io.IOException;
import java.util.HashMap;

public class Cache {
	private Cache() {}
	
	private static HashMap<String, jog.Image> images = new HashMap<String, jog.Image>();
	
	public static jog.Image image(String path) {
		path = "gfx/" + path;
		if (!images.containsKey(path)) {
			 try {
				images.put(path, new jog.Image(path));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return images.get(path);
	}
	
}
