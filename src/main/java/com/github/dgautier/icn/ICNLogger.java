package com.github.dgautier.icn;

import com.ibm.ecm.extension.PluginLogger;

import javax.servlet.ServletRequest;

/**
 * Created by DGA on 21/01/2015.
 */
public class ICNLogger {

    private final PluginLogger logger;
    private final ServletRequest request;


    public ICNLogger(PluginLogger logger, ServletRequest request) {
        this.logger = logger;
        this.request = request;
    }

    public void debug(Object object, String methodName, String text) {
        this.logger.logDebug(object, methodName, this.request, text);

    }


    public void error(Object object, String methodName, String text) {
        this.logger.logError(object, methodName, this.request, text);
    }

    public void warn(Object object, String methodName, String text) {
        this.logger.logWarning(object, methodName, this.request, text);
    }

    public void info(Object object, String methodName, String text) {
        this.logger.logInfo(object, methodName, this.request, text);
    }
}
