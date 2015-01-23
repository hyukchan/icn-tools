package com.github.dgautier.icn.model.json;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.zip.GZIPOutputStream;

/**
 * Created by DGA on 22/01/2015.
 */
public class JsonUtils {

    public static String getConfiguration(String configuration, String key) throws Exception {
        JSONObject configurationJSON = JSONObject.parse(configuration);
        JSONArray array = (JSONArray) configurationJSON.get("configuration");
        for (Object entry : array) {
            JSONObject jsonObject = (JSONObject) entry;
            String name = (String) jsonObject.get("name");
            if (name.equalsIgnoreCase(key)) {
                return (String) jsonObject.get("value");
            }
        }

        throw new IllegalArgumentException("No configuration found for key=" + key);
    }
}
