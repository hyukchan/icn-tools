package com.github.dgautier.icn.model.json.response;

import com.github.dgautier.icn.ICNLogger;
import com.ibm.ecm.json.JSONResponse;

/**
 * Created by DGA on 13/02/2015.
 */
public abstract class AbstractResponse {

    private final JSONResponse jsonResponse;
    private final ICNLogger logger;

    AbstractResponse(ICNLogger logger, JSONResponse jsonResponse) {
        this.logger = logger;
        this.jsonResponse = jsonResponse;
    }

    protected JSONResponse getJsonResponse() {
        return jsonResponse;
    }

    protected ICNLogger getLogger() {
        return logger;
    }
}
