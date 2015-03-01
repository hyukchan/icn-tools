package com.github.dgautier.icn.model.json.response;

import com.github.dgautier.icn.ICNLogger;
import com.github.dgautier.icn.model.json.JsonObject;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.json.java.JSONObject;

/**
 * Created by DGA on 13/02/2015.
 */
public abstract class AbstractResponse<T extends JSONResponse> extends JsonObject<T> {


    protected AbstractResponse(ICNLogger logger, T jsonObject) {
        super(logger, jsonObject);
    }
}
