package com.github.dgautier.icn.model.json;

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


    Criteria(ICNLogger logger, JSONObject jsonObject) {
        super(logger, jsonObject);
    }

    public static Criteria createFromJson(ICNLogger logger, JSONObject jsonObject) {
        return new Criteria(logger, jsonObject);
    }


    public Criteria hasDependentAttributes(Boolean hasDependentAttributes) {
        this.LOGGER.debug(Criteria.class, "setHasDependentProperties", "hasDependentAttributes=" + hasDependentAttributes + " on criteria=" + getSymbolicName());
        getJsonObject().put("hasDependentAttributes", hasDependentAttributes);
        getJsonObject().put("updatedHasDependentAttributes", hasDependentAttributes);
        getJsonObject().put("updated", Boolean.TRUE);
        return this;
    }

    public Criteria readOnly(Boolean readOnly) {
        this.LOGGER.debug(Criteria.class, "readOnly", "readOnly=" + readOnly + " on criteria=" + getSymbolicName());
        getJsonObject().put("readOnly", readOnly);
        getJsonObject().put("updatedReadOnly", Boolean.TRUE);
        getJsonObject().put("updated", Boolean.TRUE);
        return this;
    }

    public Criteria required(Boolean required) {
        this.LOGGER.debug(Criteria.class, "required", "required=" + required + " on criteria=" + getSymbolicName());
        getJsonObject().put("required", required);
        getJsonObject().put("updatedRequired", Boolean.TRUE);
        getJsonObject().put("updated", Boolean.TRUE);
        return this;
    }

    public Criteria hidden(Boolean hidden) {
        this.LOGGER.debug(Criteria.class, "hidden", "hidden=" + hidden + " on criteria=" + getSymbolicName());
        getJsonObject().put("hidden", hidden);
        getJsonObject().put("updatedHidden", Boolean.TRUE);
        getJsonObject().put("updated", Boolean.TRUE);
        return this;
    }

    public Criteria dataType(DataType dataType) {
        this.LOGGER.debug(Criteria.class, "dataType", "dataType=" + dataType + " on criteria=" + getSymbolicName());
        getJsonObject().put("dataType", dataType.value());
        getJsonObject().put("updated", Boolean.TRUE);
        return this;
    }

    public Criteria propertyEditor(String propertyEditor) {
        this.LOGGER.debug(Criteria.class, "propertyEditor", "propertyEditor=" + propertyEditor + " on criteria=" + getSymbolicName());
        getJsonObject().put("propertyEditor", propertyEditor);
        getJsonObject().put("updated", Boolean.TRUE);
        return this;
    }


    public Criteria values(Object values) {
        this.LOGGER.debug(Criteria.class, "values", "values=" + values + " on criteria=" + getSymbolicName());
        getJsonObject().put("values", values);
        getJsonObject().put("updatedValue", Boolean.TRUE);
        getJsonObject().put("updated", Boolean.TRUE);
        return this;
    }

    public Criteria value(Object value) {
        this.LOGGER.debug(Criteria.class, "value", "value=" + value + " on criteria=" + getSymbolicName());
        getJsonObject().put("value", value);
        getJsonObject().put("updatedValue", Boolean.TRUE);
        getJsonObject().put("updated", Boolean.TRUE);
        return this;
    }

    public Criteria displayValues(Object displayValues) {
        this.LOGGER.debug(Criteria.class, "displayValues", "displayValues=" + displayValues + " on criteria=" + getSymbolicName());
        getJsonObject().put("displayValues", displayValues);
        getJsonObject().put("updated", Boolean.TRUE);
        return this;
    }

    public Criteria choiceList(Locale locale, com.filenet.api.admin.ChoiceList choiceList) {
        this.LOGGER.debug(Criteria.class, "choiceList", "choiceList=" + choiceList + " on criteria=" + getSymbolicName());
        JSONArray choiceListValues = JsonUtils.getChoiceValues(locale, choiceList);
        return choiceList(locale, choiceList.get_DisplayName(), choiceListValues);
    }

    public Criteria choiceList(Locale locale, String displayName, JSONArray choiceListValues) {
        JSONObject jsonChoiceList = JsonUtils.getChoiceList(displayName, choiceListValues);
        getJsonObject().put("choiceList", jsonChoiceList);
        getJsonObject().put("updatedChoiceList", Boolean.TRUE);
        return validValues(locale, choiceListValues);
    }

    public Criteria choiceList(Locale locale, com.filenet.api.admin.ChoiceList choiceList, Function<JSONObject, JSONObject> optionalProperty) {
        this.LOGGER.debug(Criteria.class, "choiceList", "choiceList=" + choiceList + " on criteria=" + getSymbolicName());

        JSONArray choiceListValues = JsonUtils.getChoiceValues(locale, choiceList, optionalProperty);
        return choiceList(locale, choiceList.get_DisplayName(), choiceListValues);
    }


    public Criteria validValues(Locale locale, JSONArray validValues) {

        this.LOGGER.debug(Criteria.class, "validValues", "validValues=" + validValues + " on criteria=" + getSymbolicName());
        getJsonObject().put("validValues", validValues);
        getJsonObject().put("updated", Boolean.TRUE);
        return this;
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
        if (value instanceof JSONArray) {
            JSONArray values = (JSONArray) value;
            return (String) values.get(0);
        } else {
            return (String) value;
        }
    }


}
