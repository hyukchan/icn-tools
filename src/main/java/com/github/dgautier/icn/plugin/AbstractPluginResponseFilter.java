package com.github.dgautier.icn.plugin;

import com.filenet.api.core.ObjectStore;
import com.github.dgautier.icn.ICNLogger;
import com.github.dgautier.icn.PluginUtils;
import com.github.dgautier.icn.RequestParameters;
import com.github.dgautier.icn.model.json.JsonUtils;
import com.ibm.ecm.extension.PluginResponseFilter;
import com.ibm.ecm.extension.PluginServiceCallbacks;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.json.java.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by DGA on 22/01/2015.
 */
public abstract class AbstractPluginResponseFilter extends PluginResponseFilter {

    private ICNLogger logger;
    private String service;
    private PluginServiceCallbacks callbacks;
    private HttpServletRequest request;
    private JSONResponse json;

    protected ObjectStore getObjectStore() {
        return PluginUtils.getObjectStore(getService(),getLogger(),getCallbacks(), getRequest());
    }

    /**
     * FIXME find a way to avoid having to implement this in PluginService, ResponseFilter, RequestFilter etc..
     * @param key
     * @return
     * @throws Exception
     */
    protected String getConfiguration(String key) throws Exception {
        getLogger().debug("AbstractPluginResponseFilter","getConfiguration","Configuration= " + getCallbacks().loadConfiguration());

        return JsonUtils.getConfiguration(getCallbacks().loadConfiguration(), key);
    }


    @Override
    public void filter(String service, PluginServiceCallbacks callbacks,
                       HttpServletRequest request, JSONObject jsonObject) throws Exception {

        this.service = service;
        this.callbacks = callbacks;
        this.request = request;
        this.json = (JSONResponse) jsonObject;
        this.logger = new ICNLogger(callbacks.getLogger(), request);

        PluginUtils.print(getLogger(), getRequest());
        getLogger().debug(this,"filter","filteringService="+service);
        getLogger().debug(this,"filter","jsonObject="+getJson().toString());
        filter();
    }

    // TODO create common class to handle this method
    protected String getDesktop(){
        return getRequest().getParameter("desktop");
    }
    
    public abstract void filter() throws Exception;

    protected ICNLogger getLogger() {
        return logger;
    }

    protected JSONResponse getJson() {
        return json;
    }

    protected HttpServletRequest getRequest() {
        return request;
    }

    protected PluginServiceCallbacks getCallbacks() {
        return callbacks;
    }

    public String getService() {
        return service;
    }
}

