package com.github.dgautier.icn.plugin;

import com.filenet.api.core.ObjectStore;
import com.github.dgautier.icn.ICNLogger;
import com.github.dgautier.icn.PluginUtils;
import com.github.dgautier.icn.model.json.JsonUtils;
import com.ibm.ecm.extension.PluginService;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.json.java.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by DGA on 22/01/2015.
 */
public abstract class AbstractPluginService extends PluginService {

    private ICNLogger logger;
    private PluginServiceCallbacks callbacks;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public ObjectStore getObjectStore() {
        return objectStore;
    }

    private ObjectStore objectStore;

    public void execute(com.ibm.ecm.extension.PluginServiceCallbacks callbacks, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws java.lang.Exception{
        this.logger = new ICNLogger(callbacks.getLogger(), request);
        this.callbacks = callbacks;
        this.request = request;
        this.response = response;


        this.objectStore = PluginUtils.getObjectStore(getId(), getLogger(), callbacks, request);


        execute();
    }

    protected String getConfiguration(String key) throws Exception {
        getLogger().debug("AbstractPluginService","getConfiguration","Configuration= " + getCallbacks().loadConfiguration());

        return JsonUtils.getConfiguration(getCallbacks().loadConfiguration(), key);
    }

    protected abstract void execute () throws Exception;

    public ICNLogger getLogger() {
        return logger;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public PluginServiceCallbacks getCallbacks() {
        return callbacks;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    protected void writeResponse(JSONObject jsonObject) throws Exception {
       PluginUtils.writeResponse(getRequest(),getResponse(),jsonObject.toString());
    }
}
