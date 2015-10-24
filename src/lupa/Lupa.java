/*
 * Basado en:
 *  http://freesourcecode.net/javaprojects/79887/magnifier---in-java
 */

package lupa;

import java.awt.Font;
import java.util.Enumeration;

import javax.swing.UIManager;

public class Lupa {

	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}

	public static void main(String arg[]) {

		setUIFont(new javax.swing.plaf.FontUIResource("default", Font.BOLD, 24));

		new LupaWindow("Lupa");
	}
}
