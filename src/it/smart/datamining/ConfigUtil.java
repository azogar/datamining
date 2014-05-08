package it.smart.datamining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigUtil {
    public static Map<String, String[]> getOptionValues(String separator, String configFile) {
	Map<String, String[]> values = new HashMap<String, String[]>();
	try {
	    BufferedReader reader = new BufferedReader(new FileReader(new File(configFile)));
	    String line = new String();
	    
	    Pattern pattern = Pattern.compile("(\\w*)\\s*=\\s*(.*)");
	    Matcher matcher;
	    
	    while((line = reader.readLine()) != null) {
		matcher = pattern.matcher(line);
		if(matcher.matches()) {
		    values.put(matcher.group(1), matcher.group(2).split(separator));
		}
	    }
	    reader.close();
	    
	} catch (Exception e) {
	    e.printStackTrace();
	}
	
	return values;
    }
}
