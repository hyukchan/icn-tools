package com.github.dgautier.icn.model.json.response;

import com.github.dgautier.icn.ICNLogger;
import com.github.dgautier.icn.model.json.JsonObject;
import com.google.common.base.Function;
import com.ibm.ecm.json.JSONResultSetRow;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by DGA on 23/01/2015.
 */
public class ResultSetRow<T extends JSONResultSetRow> extends JsonObject<T> {


    protected ResultSetRow(ICNLogger logger, T jsonObject) {
        super(logger, jsonObject);
    }

    public ResultSetRow setAttributeValue(String symbolicName, Function<ResultSetRow, String> function) {

        String value = function.apply(this);
        if (value != null) {
            getJsonObject().setAttributeValue(symbolicName, value);
            getLOGGER().debug(ResultSetRow.class, "setAttributeValue", "symbolicName=" + symbolicName + " value=" + value);
        }
        return this;
    }

    public ResultSetRow setAttributeDisplayValue(String symbolicName, Function<ResultSetRow, String> function) {

        if (getJsonObject().getAttributeValue(symbolicName) != null) {
            getJsonObject().setAttributeDisplayValue(symbolicName, function.apply(this));
        } else {
            getLOGGER().warn(ResultSetRow.class, "setAttributeDisplayValue", "No value for : " + symbolicName + " in " + getJsonObject().toString());
        }
        return this;
    }

    public String getDisplayValueOrValue(String symbolicName) {

        if (!isNullOrEmpty(getJsonObject().getAttributeDisplayValue(symbolicName))) {
            return getDisplayValue(symbolicName);
        } else if (getJsonObject().getAttributeValue(symbolicName) != null) {
            return (String) getValue(symbolicName);
        } else {
            getLOGGER().debug(ResultSetRow.class, "getDisplayValueOrValue", "No value for symbolicName=" + symbolicName);
            return "";
        }
    }

    public Object getValue(String symbolicName) {
        Object value = getJsonObject().getAttributeValue(symbolicName);
        getLOGGER().debug(ResultSetRow.class, "getDisplayValueOrValue", "symbolicName=" + symbolicName + ";value=" + value);
        return getJsonObject().getAttributeValue(symbolicName);
    }

    public String getDisplayValue(String symbolicName) {
        String displayValue = getJsonObject().getAttributeDisplayValue(symbolicName);
        getLOGGER().debug(ResultSetRow.class, "getDisplayValueOrValue", "symbolicName=" + symbolicName + ";displayValue=" + displayValue);
        return displayValue;
    }
}
