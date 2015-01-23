package com.github.dgautier.icn.exception;

/**
 * Created by DGA on 22/01/2015.
 */
public class InvalidPluginException extends Exception {
    public InvalidPluginException(String string,Throwable throwable) {
        super(string, throwable);
    }

    public InvalidPluginException(String string) {
        super(string);
    }
}
