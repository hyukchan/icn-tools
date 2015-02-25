package com.github.dgautier.icn.model.json.response;

import com.github.dgautier.icn.ICNLogger;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

/**
 * Created by DGA on 13/02/2015.
 */
public class DesktopResponse extends AbstractResponse {

    DesktopResponse(ICNLogger logger, JSONResponse jsonResponse) {
        super(logger, jsonResponse);
    }


    public static DesktopResponse createFromJson(ICNLogger logger, JSONResponse jsonResponse){
        return new DesktopResponse(logger,jsonResponse);
    }
    
    public DesktopResponse removeFeature(String featureId){

        JSONObject feature = getFeature(featureId);
        if (feature != null){
            getFeatures().remove(feature);
            getLogger().debug(DesktopResponse.class, "removeFeature", "featureId=" + featureId);
        }
        
        return this;
    }

    private JSONObject getFeature(String featureId) {
        JSONArray features = getFeatures();
        for (Object feature : features){
            JSONObject featureJSON = (JSONObject) feature;
            if (featureId.equals(featureJSON.get("id"))){
                getLogger().debug(DesktopResponse.class, "getFeature", "Found featureId=" + featureId);
                return featureJSON;
            }
        }

        getLogger().debug(DesktopResponse.class, "getFeature", "Not Found featureId=" + featureId);
        return null;
    }

    public JSONArray getFeatures() {
        return (JSONArray) getJsonResponse().get("features");
    }
}
