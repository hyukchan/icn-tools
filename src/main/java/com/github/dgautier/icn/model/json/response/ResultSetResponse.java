package com.github.dgautier.icn.model.json.response;

import com.github.dgautier.icn.ICNLogger;
import com.github.dgautier.icn.RequestParameters;
import com.github.dgautier.icn.model.DataType;
import com.google.common.base.Function;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.ecm.json.JSONResultSetColumn;
import com.ibm.ecm.json.JSONResultSetResponse;
import com.ibm.ecm.json.JSONResultSetRow;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

/**
 * Created by DGA on 23/01/2015.
 */
public class ResultSetResponse extends AbstractResponse {

    public ResultSetResponse(ICNLogger logger, JSONResultSetResponse jsonResultSetResponse) {
        super(logger,jsonResultSetResponse);
    }

    @Override
    protected JSONResultSetResponse getJsonResponse() {
        return (JSONResultSetResponse) super.getJsonResponse();
    }

    private boolean hasColumn(String symbolicName) {
        return getColumn(symbolicName) != null;
    }

    private JSONResultSetColumn getColumn(String symbolicName) {
        for (int columnCount = 0; columnCount < getJsonResponse().getColumnCount(); columnCount++) {
            JSONResultSetColumn column = getJsonResponse().getColumn(columnCount);
            if (column.getName().equals(symbolicName)) {
                return column;
            }
        }
        return null;
    }

    /**
     * TODO create abstract parent class to handle this method
     *
     * @return
     */
    public String getTemplateName() {
        return (String) getJsonResponse().get(RequestParameters.TEMPLATE_NAME);
    }

    /**
     * Sets the given attribute value using the given function for each row response
     */
    public void setAttributeDisplayValue(String symbolicName, Function<ResultSetRow, String> function) {

        if (hasColumn(symbolicName)) {
            for (int rowCount = 0; rowCount < getJsonResponse().getRowCount(); rowCount++) {
                ResultSetRow row = new ResultSetRow(getLogger(), getJsonResponse().getRow(rowCount));
                row.setAttributeDisplayValue(symbolicName, function);
            }
        } else {
            getLogger().debug(ResultSetResponse.class, "setAttributeDisplayValue", "Response has no column=" + symbolicName);
        }
    }

    /**
     * Sets the given attribute value using the given function for each row response
     */
    public void setAttributeValue(String symbolicName, Function<ResultSetRow, String> function) {

        for (int rowCount = 0; rowCount < getJsonResponse().getRowCount(); rowCount++) {
            ResultSetRow row = new ResultSetRow(getLogger(), getJsonResponse().getRow(rowCount));
            row.setAttributeValue(symbolicName, function);
        }
    }

    public void setDecorator(String symbolicName, String decorator) {
        if (hasColumn(symbolicName)) {
            JSONResultSetColumn column = getColumn(symbolicName);
            column.put("decorator", decorator);
        } else {
            getLogger().debug(ResultSetResponse.class, "setDecorator", "Response has no column=" + symbolicName);
        }
    }

    /**
     * *
     *
     * @param title
     * @param width        in px like : "50px"
     * @param symbolicName
     * @param style
     * @param sortable
     * @param dataType
     * @param format
     * @param value
     * @param displayValue
     */
    public void addColumn(String title, String width, String symbolicName, String style, boolean sortable, DataType dataType, String format, Function<ResultSetRow, String> value, Function<ResultSetRow, String> displayValue, boolean asFirstColumn) {
        if (hasColumn(title)) {
            throw new IllegalArgumentException("ResultSetResponse already has a column named =" + title);
        }


        for (int rowCount = 0; rowCount < getJsonResponse().getRowCount(); rowCount++) {
            JSONResultSetRow row = getJsonResponse().getRow(rowCount);
            ResultSetRow resultSetRow = new ResultSetRow(getLogger(), row);
            //addAttribute(String id, Object value, String type, String format, String displayValue)
            row.addAttribute(symbolicName, value.apply(resultSetRow), dataType.value(), format, displayValue.apply(resultSetRow));
        }

        JSONResultSetColumn newColumn = new JSONResultSetColumn(title, width, symbolicName, style, sortable);

        if (asFirstColumn) {
            JSONArray columns = new JSONArray();


            int count = 0;
            for (Object column : this.getColumns()) {


                // We had the new column as the third cause the 2 first columns are icons and status related
                if (count == 2) {
                    columns.add(newColumn);
                } else {
                    columns.add(column);
                }
                count++;

            }

            // Empty Columns 
            this.getColumns().clear();

            // Add columns
            this.getColumns().addAll(columns);

        } else {
            getJsonResponse().addColumn(newColumn);
        }
    }


    private JSONArray getColumns() {
        JSONObject structure = (JSONObject) getJsonResponse().get("columns");
        JSONArray columnSet0 = (JSONArray) structure.get("cells");
        JSONArray columns = (JSONArray) columnSet0.get(0);
        return columns;
    }

}