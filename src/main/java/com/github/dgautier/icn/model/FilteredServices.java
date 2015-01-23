package com.github.dgautier.icn.model;

/**
 * Created by DGA on 22/01/2015.
 */
public enum FilteredServices {
    
    P8_GET_WORK_ITEMS("p8","getWorkItems"),
    P8_GET_NEXT_WORK_ITEMS("p8","getNextWorkItems"),
    P8_SEARCH("p8","search"),
    P8_OPEN_SEARCH_TEMPLATE("p8","openSearchTemplate"),
    P8_GET_CONTENT_ITEMS("p8","getContentItems"),
    P8_GET_FILTER_CRITERIA("p8","getFilterCriteria"),
    P8_GET_STEP_PARAMETERS("p8","getStepParameters"),
    P8_GET_STEP_ATTACHMENT_ITEMS("p8","getStepAttachmentItems"),
    P8_GET_DEPENDANT_ATTRIBUTE_INFO("p8","getDependentAttributeInfo"),
    P8_COMPLETE_STEP("p8","completeStep"),
    P8_GET_DEPENDANT_PARAMETERS("p8","getDependentParameters"),
    P8_OPEN_ITEM("p8","openItem"),
    P8_OPEN_CONTENT_CLASS("p8","openContentClass"),
    P8_CONTINUE_QUERY("p8","continueQuery"),
    P8_EDIT_ATTRIBUTES("p8","editAttributes"),
    P8_OPEN_FOLDER("p8","openFolder")
    ;

    private final String serverType;
    private final String serviceName;

    FilteredServices(String serverType,String serviceName){
        this.serverType = serverType;
        this.serviceName = serviceName;
    }

    public String getServerType() {
        return serverType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getUrl() {
        String url = "/" + serverType + "/" + serviceName;
        return url;
    }

}

