package com.github.dgautier.icn.model;

/**
 * Created by DGA on 21/01/2015.
 */
public enum DataType {
    BOOLEAN("xs:boolean"),
    INTEGER("xs:integer"),
    STRING("xs:string"),
    DATE("xs:timestamp"),
    GUID("xs:guid"),
    DOUBLE("xs:double"),
    OBJECT("xs:object"),
    ATTACHMENT("xs:attachment"),
    USER("xs:user");

    private final String value;

    DataType(String value){
        this.value = value;
    }

    public String value(){
        return this.value;
    }

    public static DataType fromId(String id){
        if (BOOLEAN.value().equals(id)){
            return BOOLEAN;
        } else if (INTEGER.value().equals(id)){
            return INTEGER;
        }  else if (STRING.value().equals(id)){
            return STRING;
        }  else if (DATE.value().equals(id)){
            return DATE;
        }  else if (GUID.value().equals(id)){
            return GUID;
        }  else if (DOUBLE.value().equals(id)){
            return DOUBLE;
        }  else if (OBJECT.value().equals(id)){
            return OBJECT;
        }  else if (ATTACHMENT.value().equals(id)){
            return ATTACHMENT;
        }  else if (USER.value().equals(id)){
            return USER;
        } else {
            throw new RuntimeException("Not Implemented :" + id);
        }

    }
}

