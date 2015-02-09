package com.github.dgautier.icn.model.json.response;

import com.github.dgautier.icn.ICNLogger;
import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.ibm.ecm.json.JSONResultSetRow;

import java.util.Objects;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by DGA on 23/01/2015.
 */
public class ResultSetRow {

    public JSONResultSetRow getResultSetRow() {
        return resultSetRow;
    }

    private final JSONResultSetRow resultSetRow;
    private final ICNLogger logger;

    public ResultSetRow(ICNLogger logger, JSONResultSetRow resultSetRow) {
        this.logger = logger;
        this.resultSetRow = resultSetRow;
    }


    public void setAttributeValue(String symbolicName,  Function<ResultSetRow, String> function) {

        String value = function.apply(this);
        if (value != null) {
            this.resultSetRow.setAttributeValue(symbolicName,value);
            logger.debug(ResultSetRow.class, "setAttributeValue", "symbolicName=" + symbolicName + " value="+value);
        }


    }

    public void setAttributeDisplayValue(String symbolicName, Function<ResultSetRow, String> function) {

        if (this.resultSetRow.getAttributeValue(symbolicName) != null) {
            this.resultSetRow.setAttributeDisplayValue(symbolicName, function.apply(this));
        } else {
            logger.warn(ResultSetRow.class, "setAttributeDisplayValue", "No value for : " + symbolicName + " in " + this.resultSetRow.toString());
        }
    }

    public String getDisplayValueOrValue(String symbolicName) {

        if (!isNullOrEmpty(this.resultSetRow.getAttributeDisplayValue(symbolicName))) {
            return getDisplayValue(symbolicName);
        } else if (this.resultSetRow.getAttributeValue(symbolicName) != null) {
            return (String) getValue(symbolicName);
        } else {
            logger.debug(ResultSetRow.class, "getDisplayValueOrValue", "No value for symbolicName=" + symbolicName);
            return "";
        }
    }

    public Object getValue(String symbolicName) {
        Object value = this.resultSetRow.getAttributeValue(symbolicName);
        logger.debug(ResultSetRow.class, "getDisplayValueOrValue", "symbolicName=" + symbolicName + ";value=" + value);
        return this.resultSetRow.getAttributeValue(symbolicName);
    }

    public String getDisplayValue(String symbolicName) {
        String displayValue = this.resultSetRow.getAttributeDisplayValue(symbolicName);
        logger.debug(ResultSetRow.class, "getDisplayValueOrValue", "symbolicName=" + symbolicName + ";displayValue=" + displayValue);
        return displayValue;
    }
}
