package eu.agentsunited.topicselectionengine;

import eu.woolplatform.utils.AppComponents;
import eu.woolplatform.utils.exception.ParseException;
import eu.woolplatform.utils.http.HttpURL;

public class ServiceContext {
	/**
	 * Returns the base URL.
	 * 
	 * @return the base URL
	 */
	public static String getBaseUrl() {
		Configuration config = AppComponents.get(Configuration.class);
		return config.get(Configuration.BASE_URL);
	}
	
	/**
	 * Returns the base path.
	 * 
	 * @return the base path
	 */
	public static String getBasePath() {
		String url = getBaseUrl();
		HttpURL httpUrl;
		try {
			httpUrl = HttpURL.parse(url);
		} catch (ParseException ex) {
			throw new RuntimeException(
					"Invalid base URL: " + url + ": " + ex.getMessage(), ex);
		}
		return httpUrl.getPath();
	}
}
