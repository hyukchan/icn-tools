package com.github.dgautier.icn.plugin;

import com.github.dgautier.icn.model.ServerTypes;
import com.ibm.ecm.extension.PluginAction;

/**
 * Created by DGA on 22/01/2015.
 * TODO Improve genericity
 */
public abstract class AbstractPluginAction extends PluginAction {


    /* (non-Javadoc)
     * @see com.ibm.ecm.extension.PluginAction#getId()
     */
    @Override
    public String getId() {
        return getClass().getCanonicalName();
    }

    /*
     * (non-Javadoc)
     * @see com.ibm.ecm.extension.PluginAction#getServerTypes()
     */
    @Override
    public String getServerTypes() {
        return ServerTypes.FILENET;
    }
}
