package com.github.dgautier.icn.plugin;

import com.google.common.base.Strings;
import com.ibm.ecm.configuration.ApplicationConfig;
import com.ibm.ecm.configuration.Config;
import com.ibm.ecm.configuration.DesktopConfig;
import com.ibm.ecm.configuration.MenuConfig;
import com.ibm.ecm.extension.*;

import java.util.Collection;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by DGA on 22/01/2015.
 * TODO Improve genericity
 */
public abstract class AbstractPlugin extends Plugin {

    private static PluginLogger pluginLogger = null;
    /**
     * Private Fields will not be reflected in json
     * Only protected Field will be
     */
    

    /**
     * Only used by icn maven plugin
     */
    protected String filename;
    protected String configClass;
    protected String configuration;

    public AbstractPlugin() {
        pluginLogger = new PluginLogger(this);
    }

    @Override
    public String getId() {
        return this.getClass().getSimpleName() + "-" + getVersion();
    }

    public final String getFilename() {
        return this.filename;
    }


    public final void setFilename(String filename) {
        this.filename = filename;
    }

    public String getConfigClass() {
        return configClass;
    }

    protected void setConfigClass(String configClass) {
        this.configClass = configClass;
    }


    public String getConfiguration() {
        return configuration;
    }


    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getConfigurationDijitClass() {
        if (isNullOrEmpty(getDojoModule())){
            return null;
        } else {
            return getDojoModule() + ".ConfigurationPane";    
        }
    }

    /* (non-Javadoc)
     * @see com.ibm.ecm.extension.Plugin#getVersion()
     */
    @Override
    public String getVersion() {
        return getClass().getPackage().getImplementationVersion();
    }
    
    protected boolean isDesktopUnique(ApplicationConfig appConfig, String desktopId) {
        Collection<DesktopConfig> desktopConfigs = appConfig.getDesktopList();

        for (DesktopConfig desktopConfig : desktopConfigs) {
            if (desktopConfig.getObjectId().equals(desktopId))
                return false;
        }
        return true;
    }

    protected boolean doesMenuExist(String[] menuIds, String menuId) {
        for (String id : menuIds) {
            if (menuId.equalsIgnoreCase(id))
                return true;
        }
        return false;
    }

    protected void createMenuConfig(MenuConfig menuConfig)
            throws Exception {
        String[] actions = menuConfig.getItems();
        String[] newActions = new String[actions.length + 1];
        for (int i = 0; i < actions.length; i++) {
            newActions[i] = actions[i];
        }
        menuConfig.setItems(newActions);
        menuConfig.save();

    }
}
