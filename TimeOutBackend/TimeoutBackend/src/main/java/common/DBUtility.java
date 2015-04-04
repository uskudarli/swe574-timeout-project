package common;

import java.util.HashMap;
import java.util.Map;

import com.google.appengine.api.utils.SystemProperty;

public class DBUtility {
	public static Map<String, String> putProperties() {
		Map<String, String> properties = new HashMap<>();
        if (SystemProperty.environment.value() ==
                SystemProperty.Environment.Value.Production) {
            properties.put("javax.persistence.jdbc.driver",
                    "com.mysql.jdbc.GoogleDriver");
            properties.put("javax.persistence.jdbc.url",
                    "jdbc:google:mysql://vernal-day-88222:instance2/demo?user=root");
        } else {
            properties.put("javax.persistence.jdbc.driver",
                    "com.mysql.jdbc.Driver");
            properties.put("javax.persistence.jdbc.url",
                    "jdbc:mysql://localhost:3306/demo?user=root?password=root");
        }
        
        return properties;
	}
}
