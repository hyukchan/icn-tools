package com.github.dgautier.icn.model.json.response;

import com.github.dgautier.icn.ICNLogger;
import com.github.dgautier.icn.RequestParameters;
import com.google.common.base.Function;
import com.ibm.ecm.json.JSONResultSetColumn;
import com.ibm.ecm.json.JSONResultSetResponse;
import com.ibm.ecm.json.JSONResultSetRow;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

/**
 * Created by DGA on 23/01/2015.
 */
public class ResultSetResponse<T extends JSONResultSetResponse> extends AbstractResponse<JSONResultSetResponse> {


    public ResultSetResponse(ICNLogger logger, T jsonResponse) {
        super(logger, jsonResponse);
    }

    public ResultSetResponse(ICNLogger logger){
        super(logger, new JSONResultSetResponse());
    }

    private boolean hasColumn(String symbolicName) {
        return getColumn(symbolicName) != null;
    }

    private JSONResultSetColumn getColumn(String symbolicName) {
        for (int columnCount = 0; columnCount < getJsonObject().getColumnCount(); columnCount++) {
            JSONResultSetColumn column = getJsonObject().getColumn(columnCount);
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
        return (String) getJsonObject().get(RequestParameters.TEMPLATE_NAME);
    }

    /**
     * Sets the given attribute value using the given function for each row response
     */
    public ResultSetResponse setAttributeDisplayValue(String symbolicName, Function<ResultSetRow, String> function) {

        if (hasColumn(symbolicName)) {
            for (int rowCount = 0; rowCount < getJsonObject().getRowCount(); rowCount++) {
                ResultSetRow row = new ResultSetRow(getLOGGER(), getJsonObject().getRow(rowCount));
                row.setAttributeDisplayValue(symbolicName, function);
            }
        } else {
            getLOGGER().debug(ResultSetResponse.class, "setAttributeDisplayValue", "Response has no column=" + symbolicName);
        }

        return this;
    }

    /**
     * Sets the given attribute value using the given function for each row response
     */
    public ResultSetResponse setAttributeValue(String symbolicName, Function<ResultSetRow, String> function) {

        for (int rowCount = 0; rowCount < getJsonObject().getRowCount(); rowCount++) {
            ResultSetRow row = new ResultSetRow(getLOGGER(), getJsonObject().getRow(rowCount));
            row.setAttributeValue(symbolicName, function);
        }

        return this;
    }

    public ResultSetResponse setDecorator(String symbolicName, String decorator) {
        if (hasColumn(symbolicName)) {
            JSONResultSetColumn column = getColumn(symbolicName);
            column.put("decorator", decorator);
        } else {
            getLOGGER().debug(ResultSetResponse.class, "setDecorator", "Response has no column=" + symbolicName);
        }
        return this;
    }

    /**
     * *
     *
     * @param resultSetColumn
     * @param value
     * @param displayValue
     */
    public ResultSetResponse addColumn(ResultSetColumn resultSetColumn, Function<ResultSetRow, String> value, Function<ResultSetRow, String> displayValue, boolean asFirstColumn) {
        if (hasColumn(resultSetColumn.name())) {
            throw new IllegalArgumentException("ResultSetResponse already has a column named =" + resultSetColumn.name());
        }


        for (int rowCount = 0; rowCount < getJsonObject().getRowCount(); rowCount++) {
            addNewColumnValueToRow(resultSetColumn, value, displayValue, rowCount);
        }


        if (asFirstColumn) {
            addAsFistColumn(resultSetColumn);

        } else {
            getJsonObject().addColumn(resultSetColumn.json());
        }

        return this;
    }

    private void addNewColumnValueToRow(ResultSetColumn resultSetColumn, Function<ResultSetRow, String> value, Function<ResultSetRow, String> displayValue, int rowCount) {
        JSONResultSetRow row = getJsonObject().getRow(rowCount);
        ResultSetRow resultSetRow = new ResultSetRow(getLOGGER(), row);
        row.addAttribute(resultSetColumn.field(), value.apply(resultSetRow), resultSetColumn.getDataType().value(), resultSetColumn.getFormat(), displayValue.apply(resultSetRow));
    }

    private void addAsFistColumn(ResultSetColumn resultSetColumn) {
        JSONArray columns = new JSONArray();


        int count = 0;
        for (Object column : this.getColumns()) {


            // We had the new column as the third cause the 2 first columns are icons and status related
            if (count == 2) {
                columns.add(resultSetColumn.json());
            } else {
                columns.add(column);
            }
            count++;

        }

        // Empty Columns
        this.getColumns().clear();

        // Add columns
        this.getColumns().addAll(columns);
    }


    private JSONArray getColumns() {
        JSONObject structure = (JSONObject) getJsonObject().get("columns");
        JSONArray columnSet0 = (JSONArray) structure.get("cells");
        JSONArray columns = (JSONArray) columnSet0.get(0);
        return columns;
    }

}