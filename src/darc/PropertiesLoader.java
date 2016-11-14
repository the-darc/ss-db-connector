package darc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
	
	private final Properties properties;
	
	public PropertiesLoader() throws IOException {
		InputStream input = null;
		this.properties = new Properties();

		try {
			input = PropertiesLoader.class.getClassLoader().getResourceAsStream("config/db.properties");
			this.properties.load(input);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public String get(String key) {
		return this.properties.getProperty(key);
	}
}
