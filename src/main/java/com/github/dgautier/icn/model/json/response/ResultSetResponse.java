package com.github.dgautier.icn.model.json.response;

import com.github.dgautier.icn.ICNLogger;
import com.github.dgautier.icn.model.DataType;
import com.google.common.base.Function;
import com.ibm.ecm.json.JSONResultSetColumn;
import com.ibm.ecm.json.JSONResultSetResponse;
import com.ibm.ecm.json.JSONResultSetRow;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

/**
 * Created by DGA on 23/01/2015.
 */
public class ResultSetResponse {

    private final JSONResultSetResponse jsonResultSetResponse;
    private final ICNLogger logger;

    public ResultSetResponse(ICNLogger logger,JSONResultSetResponse jsonResultSetResponse) {
        this.logger = logger;
        this.jsonResultSetResponse = jsonResultSetResponse;
    }


    private boolean hasColumn(String symbolicName) {
        return getColumn(symbolicName) != null;
    }
    
    private JSONResultSetColumn getColumn(String symbolicName) {
        for (int columnCount = 0; columnCount < this.jsonResultSetResponse.getColumnCount(); columnCount++) {
            JSONResultSetColumn column = this.jsonResultSetResponse.getColumn(columnCount);
            if (column.getName().equals(symbolicName)) {
                return column;
            }
        }
        return null;
    }

    /**
     * Sets the given attribute value using the given function for each row response
     */
    public void setAttributeDisplayValue(String symbolicName, Function<ResultSetRow, String> function) {

        if (hasColumn(symbolicName)) {
            for (int rowCount = 0; rowCount < this.jsonResultSetResponse.getRowCount(); rowCount++) {
                ResultSetRow row = new ResultSetRow(this.logger,this.jsonResultSetResponse.getRow(rowCount));
                row.setAttributeDisplayValue(symbolicName, function);
            }
        } else {
            this.logger.debug(ResultSetResponse.class,"setAttributeDisplayValue","Response has no column="+symbolicName);
        }
    }

    public void setDecorator(String symbolicName, String decorator) {
        if (hasColumn(symbolicName)) {
            JSONResultSetColumn column = getColumn(symbolicName);
            column.put("decorator", decorator);
        } else {
            this.logger.debug(ResultSetResponse.class, "setDecorator", "Response has no column=" + symbolicName);
        }
    }

    /**
     * *
     * @param title
     * @param width in px lika : "50px"
     * @param symbolicName
     * @param style
     * @param sortable
     * @param dataType
     * @param format
     * @param value
     * @param displayValue
     */
    public void addColumn (String title,String width, String symbolicName, String style,boolean sortable,DataType dataType,String format,  Function<ResultSetRow, String> value,Function<ResultSetRow, String> displayValue,boolean asFirstColumn){
        if (hasColumn(title)){
            throw new IllegalArgumentException("ResultSetResponse already has a column named ="+title);
        }


        for (int rowCount = 0; rowCount < this.jsonResultSetResponse.getRowCount(); rowCount++) {
            JSONResultSetRow row = this.jsonResultSetResponse.getRow(rowCount);
            ResultSetRow resultSetRow = new ResultSetRow(this.logger,row);
            //addAttribute(String id, Object value, String type, String format, String displayValue)
            row.addAttribute(symbolicName, value.apply(resultSetRow),dataType.value(),format,displayValue.apply(resultSetRow));
        }
 
        JSONResultSetColumn newColumn = new JSONResultSetColumn(title,width,symbolicName,style,sortable);
        
        if (asFirstColumn){
            JSONArray columns = new JSONArray();
            

            int count = 0;
            for (Object column : this.getColumns()){
            
                
                // We had the new column as the third cause the 2 first columns are icons and status related
                if (count == 2){
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
            this.jsonResultSetResponse.addColumn(newColumn);
        }
    }


    private JSONArray getColumns() {
        JSONObject structure = (JSONObject)this.jsonResultSetResponse.get("columns");
        JSONArray columnSet0 = (JSONArray)structure.get("cells");
        JSONArray columns = (JSONArray)columnSet0.get(0);
        return columns;
    }
    
}