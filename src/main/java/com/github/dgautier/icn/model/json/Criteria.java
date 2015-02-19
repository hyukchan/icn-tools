package com.github.dgautier.icn.model.json;

import com.filenet.api.admin.ChoiceList;
import com.github.dgautier.icn.ICNLogger;
import com.github.dgautier.icn.model.DataType;
import com.google.common.base.Function;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import java.util.Locale;

/**
 * Created by DGA on 21/01/2015.
 */
public class Criteria extends JsonObject {


    public Criteria(ICNLogger logger, JSONObject jsonObject) {
        super(logger, jsonObject);
    }


    public void setHasDependentAttributes(Boolean hasDependentAttributes) {
        this.LOGGER.debug(Criteria.class, "setHasDependentProperties", "hasDependentAttributes=" + hasDependentAttributes + " on criteria=" + getSymbolicName());
        getJsonObject().put("hasDependentAttributes", hasDependentAttributes);
        getJsonObject().put("updatedHasDependentAttributes", hasDependentAttributes);
        getJsonObject().put("updated", Boolean.TRUE);
    }

    public void setReadOnly(Boolean readOnly) {
        this.LOGGER.debug(Criteria.class, "setReadOnly", "readOnly=" + readOnly + " on criteria=" + getSymbolicName());
        getJsonObject().put("readOnly", readOnly);
        getJsonObject().put("updatedReadOnly", Boolean.TRUE);
        getJsonObject().put("updated", Boolean.TRUE);
    }

    public void setRequired(Boolean required) {
        this.LOGGER.debug(Criteria.class, "setRequired", "required=" + required + " on criteria=" + getSymbolicName());
        getJsonObject().put("required", required);
        getJsonObject().put("updatedRequired", Boolean.TRUE);
        getJsonObject().put("updated", Boolean.TRUE);
    }

    public void setHidden(Boolean hidden) {
        this.LOGGER.debug(Criteria.class, "setHidden", "hidden=" + hidden + " on criteria=" + getSymbolicName());
        getJsonObject().put("hidden", hidden);
        getJsonObject().put("updatedHidden", Boolean.TRUE);
        getJsonObject().put("updated", Boolean.TRUE);
    }

    public void setDataType(DataType dataType) {
        this.LOGGER.debug(Criteria.class, "setDataType", "dataType=" + dataType + " on criteria=" + getSymbolicName());
        getJsonObject().put("dataType", dataType.value());
        getJsonObject().put("updated", Boolean.TRUE);
    }

    public void setPropertyEditor(String propertyEditor) {
        this.LOGGER.debug(Criteria.class, "setPropertyEditor", "propertyEditor=" + propertyEditor + " on criteria=" + getSymbolicName());
        getJsonObject().put("propertyEditor", propertyEditor);
        getJsonObject().put("updated", Boolean.TRUE);
    }


    public void setValues(Object values) {
        this.LOGGER.debug(Criteria.class, "setValues", "values=" + values + " on criteria=" + getSymbolicName());
        getJsonObject().put("values", values);
        getJsonObject().put("updatedValue", Boolean.TRUE);
        getJsonObject().put("updated", Boolean.TRUE);
    }

    public void setValue(Object value) {
        this.LOGGER.debug(Criteria.class, "setValue", "value=" + value + " on criteria=" + getSymbolicName());
        getJsonObject().put("value", value);
        getJsonObject().put("updatedValue", Boolean.TRUE);
        getJsonObject().put("updated", Boolean.TRUE);
    }

    public void setDisplayValues(Object displayValues) {
        this.LOGGER.debug(Criteria.class, "setDisplayValues", "displayValues=" + displayValues + " on criteria=" + getSymbolicName());
        getJsonObject().put("displayValues", displayValues);
        getJsonObject().put("updated", Boolean.TRUE);
    }

    public void setChoiceList(Locale locale, com.filenet.api.admin.ChoiceList choiceList) {
        this.LOGGER.debug(Criteria.class, "setChoiceList", "setChoiceList=" + choiceList + " on criteria=" + getSymbolicName());
        JSONArray choiceListValues = JsonUtils.getChoiceValues(locale, choiceList);
        setChoiceList(locale, choiceList.get_DisplayName(), choiceListValues);
    }

    public void setChoiceList(Locale locale, String displayName, JSONArray choiceListValues) {
        JSONObject jsonChoiceList = JsonUtils.getChoiceList(displayName, choiceListValues);
        getJsonObject().put("choiceList", jsonChoiceList);
        getJsonObject().put("updatedChoiceList", Boolean.TRUE);
        setValidValues(locale, choiceListValues);
    }

    public void setChoiceList(Locale locale, com.filenet.api.admin.ChoiceList choiceList, Function<JSONObject, JSONObject> optionalProperty) {
        this.LOGGER.debug(Criteria.class, "setChoiceList", "setChoiceList=" + choiceList + " on criteria=" + getSymbolicName());

        JSONArray choiceListValues = JsonUtils.getChoiceValues(locale, choiceList, optionalProperty);
        setChoiceList(locale, choiceList.get_DisplayName(), choiceListValues);
    }


    public void setValidValues(Locale locale, JSONArray validValues) {

        this.LOGGER.debug(Criteria.class, "setValidValues", "setValidValues=" + validValues + " on criteria=" + getSymbolicName());
        getJsonObject().put("validValues", validValues);
        getJsonObject().put("updated", Boolean.TRUE);
    }

    public Object getValue() {

        if (getJson().get("values") != null) {
            Object value = getJson().get("values");
            this.LOGGER.debug(Criteria.class, "getValue", "getValue=" + value + " on criteria=" + getSymbolicName());
            return value;
        } else {
            Object value = getJson().get("value");
            this.LOGGER.debug(Criteria.class, "getValue", "getValue=" + value + " on criteria=" + getSymbolicName());
            return value;
        }
    }

    public String getFirstValue() {

        Object value = getValue();
        if (value instanceof JSONArray){
            JSONArray values = (JSONArray) value;
            return (String) values.get(0);
        } else {
            return (String) value;
        }
    }


}
