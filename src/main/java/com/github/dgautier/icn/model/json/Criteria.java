package com.github.dgautier.icn.model.json;

import com.filenet.api.admin.LocalizedString;
import com.filenet.api.collection.ChoiceList;
import com.filenet.api.collection.LocalizedStringList;
import com.filenet.api.constants.TypeID;
import com.github.dgautier.icn.ICNLogger;
import com.github.dgautier.icn.model.DataType;
import com.google.common.base.Function;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import java.util.Iterator;
import java.util.Locale;

/**
 * Created by DGA on 21/01/2015.
 */
public class Criteria extends JsonObject {


    public Criteria(ICNLogger logger, JSONObject jsonObject) {
        super(logger, jsonObject);
    }

    public void setReadOnly(Boolean readOnly) {
        this.LOGGER.debug(Criteria.class, "setReadOnly", "readOnly=" + readOnly + " on criteria=" + getSymbolicName());
        getJsonObject().put("readOnly", readOnly);
    }

    public void setRequired(Boolean required) {
        this.LOGGER.debug(Criteria.class, "setRequired", "required=" + required + " on criteria=" + getSymbolicName());
        getJsonObject().put("required", required);
    }

    public void setHidden(Boolean hidden) {
        this.LOGGER.debug(Criteria.class, "setHidden", "hidden=" + hidden + " on criteria=" + getSymbolicName());
        getJsonObject().put("hidden", hidden);
    }

    public void setDataType(DataType dataType) {
        this.LOGGER.debug(Criteria.class, "setDataType", "dataType=" + dataType + " on criteria=" + getSymbolicName());
        getJsonObject().put("dataType", dataType.value());
    }

    public void setPropertyEditor(String propertyEditor) {
        this.LOGGER.debug(Criteria.class, "setPropertyEditor", "propertyEditor=" + propertyEditor + " on criteria=" + getSymbolicName());
        getJsonObject().put("propertyEditor", propertyEditor);
    }


    public void setValues(Object values) {
        this.LOGGER.debug(Criteria.class, "setValues", "values=" + values + " on criteria=" + getSymbolicName());
        getJsonObject().put("values", values);
    }

    public void setDisplayValues(Object displayValues) {
        this.LOGGER.debug(Criteria.class, "setDisplayValues", "displayValues=" + displayValues + " on criteria=" + getSymbolicName());
        getJsonObject().put("displayValues", displayValues);
    }

    public void setChoiceList(Locale locale, com.filenet.api.admin.ChoiceList choiceList) {
        this.LOGGER.debug(Criteria.class, "setChoiceList", "setChoiceList=" + choiceList + " on criteria=" + getSymbolicName());
        JSONArray choiceListValues = JsonUtils.getChoiceValues(locale, choiceList, null);
        JSONObject jsonChoiceList = JsonUtils.getChoiceList(choiceList, choiceListValues);
        getJsonObject().put("choiceList", jsonChoiceList);
        setValidValues(locale,choiceListValues);
    }

    public void setChoiceList(Locale locale, com.filenet.api.admin.ChoiceList choiceList,Function<JSONObject,JSONObject> optionalProperty) {
        this.LOGGER.debug(Criteria.class, "setChoiceList", "setChoiceList=" + choiceList + " on criteria=" + getSymbolicName());

        JSONArray choiceListValues = JsonUtils.getChoiceValues(locale, choiceList, optionalProperty);
        JSONObject jsonChoiceList = JsonUtils.getChoiceList(choiceList, choiceListValues);
        getJsonObject().put("choiceList", jsonChoiceList);
        setValidValues(locale,choiceListValues);
    }



    public void setValidValues(Locale locale, JSONArray validValues) {

        this.LOGGER.debug(Criteria.class, "setValidValues", "setValidValues=" + validValues + " on criteria=" + getSymbolicName());
        getJsonObject().put("validValues", validValues);
    }



}
