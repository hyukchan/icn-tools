package com.github.dgautier.icn.model.json.response;

import com.github.dgautier.icn.ICNLogger;
import com.github.dgautier.icn.model.DataType;
import com.ibm.ecm.json.JSONResultSetColumn;

/**
 * Created by dgautier on 2/25/2015.
 */
public class ResultSetColumn {


    private final ICNLogger logger;
    private final JSONResultSetColumn resultSetColumn;

    // Attributes used to build each row for this column 
    private DataType dataType;
    private String format;

    ResultSetColumn(ICNLogger logger) {
        this.logger = logger;
        this.resultSetColumn = new JSONResultSetColumn();
    }
    
    ResultSetColumn(ICNLogger logger,JSONResultSetColumn resultSetColumn) {
        this.logger = logger;
        this.resultSetColumn = resultSetColumn;
    }

    public static ResultSetColumn create(ICNLogger logger) {
        return new ResultSetColumn(logger);
    }

    public static ResultSetColumn createFromJson(ICNLogger logger,JSONResultSetColumn resultSetColumn) {
        return new ResultSetColumn(logger,resultSetColumn);
    }

    public ResultSetColumn name(String name) {
        this.resultSetColumn.setName(name);
        return this;
    }

    /**
     * css width in px : 140px
     *
     * @param width
     * @return
     */
    public ResultSetColumn width(String width) {
        this.resultSetColumn.setWidth(width);
        return this;
    }

    /**
     * symbolicName of the column
     *
     * @param field
     * @return
     */
    public ResultSetColumn field(String field) {
        this.resultSetColumn.setField(field);
        return this;
    }

    public ResultSetColumn style(String style) {
        this.resultSetColumn.put("style", style);
        return this;
    }

    public ResultSetColumn sortable(boolean sortable) {
        this.resultSetColumn.setSortable(sortable);
        return this;
    }

    public ResultSetColumn dataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public ResultSetColumn format(String format) {
        this.format = format;
        return this;
    }

    public String getFormat() {
        return format;
    }

    DataType getDataType() {
        return dataType;
    }

    public String name() {
        return this.resultSetColumn.getName();
    }

    public String field() {
        return this.resultSetColumn.getField();
    }
    
    public JSONResultSetColumn json(){
        return this.resultSetColumn;
    }
}
