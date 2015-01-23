package com.github.dgautier.icn.model.json;

import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.property.*;
import com.filenet.api.security.Group;
import com.filenet.api.security.User;
import com.filenet.apiimpl.property.PropertyEngineObjectListImpl;
import com.github.dgautier.icn.ICNLogger;
import com.google.common.base.Strings;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by DGA on 21/01/2015.
 */
public class JsonObject {

    protected static ICNLogger LOGGER;

    private final JSONObject jsonObject;

    public JsonObject(ICNLogger logger, JSONObject jsonObject) {
        this.LOGGER = logger;
        this.jsonObject = jsonObject;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public static JSONObject toJSONObject(ICNLogger logger, IndependentObjectSet independentObjectSet, int pageSize, final String orderingValue) {
        return toJsonObject(logger, pageSize, independentObjectSet.iterator(), orderingValue);
    }

    public static JSONObject toJSONObject(ICNLogger logger, List<IndependentObject> independentObjects, int pageSize, final String orderingValue) {
        return toJsonObject(logger, pageSize, independentObjects.iterator(), orderingValue);
    }


    private static JSONObject toJsonObject(ICNLogger logger, int pageSize, Iterator it, final String orderingValue) {
        final JSONObject jsonObject = new JSONObject();
        JSONArray jsonItems = new JSONArray();
        int i = 1;
        while (it.hasNext()) {
            JSONObject jsonObj = new JSONObject();
            IndependentObject io = (IndependentObject) it.next();
            if (io instanceof User) {
                User user = (User) io;
                addUser(jsonObj, user);
            } else if (io instanceof Group) {
                Group group = (Group) io;
                addGroup(logger, jsonObj, group, orderingValue);
            } else if (io instanceof CustomObject) {
                CustomObject customObject = (CustomObject) io;
                addCustomObject(logger, jsonObj, customObject.getProperties());
            }

            jsonItems.add(jsonObj);

            if (i == pageSize)
                break;
            i++;
        }

        if (!Strings.isNullOrEmpty(orderingValue)) {
            jsonItems = sort(logger, jsonItems, orderingValue);
        }

        jsonObject.put("items", jsonItems);
        jsonObject.put("length", jsonItems.size());

        logger.info(JsonObject.class, "toJsonObject", "Found " + (i - 1) + " results");
        return jsonObject;
    }

    private static void addCustomObject(ICNLogger logger, JSONObject jsonObj, Properties properties) {
        for (Iterator<Property> propertyIterator = properties.iterator(); propertyIterator.hasNext(); ) {
            Property property = propertyIterator.next();

            logger.debug(JsonObject.class, "addCustomObject", "CustomObject Property name : " + property.getPropertyName());
            logger.debug(JsonObject.class, "addCustomObject", "CustomObject Property class : " + property.getClass().getCanonicalName());

            try {
                if (property instanceof PropertyString) {
                    jsonObj.put(property.getPropertyName(), property.getStringValue());
                } else if (property instanceof PropertyId) {

                    if (PropertyNames.ID.equals(property.getPropertyName())) {
                        // We need to have an id cause of the use of "ecm/model/_ModelStore"
                        jsonObj.put(PropertyNames.ID.toLowerCase(), property.getIdValue().toString());
                    }

                    if (property.getIdValue() != null) {
                        jsonObj.put(property.getPropertyName(), property.getIdValue().toString());
                    }

                } else if (property instanceof PropertyEngineObject
                        || property instanceof PropertyEngineObjectListImpl) {
                    logger.warn(JsonObject.class, "addCustomObject", "Property=" + property.getPropertyName());
                    logger.warn(JsonObject.class, "addCustomObject", "PropertyType not handled : " + property.getClass().getCanonicalName());
                } else {
                    logger.warn(JsonObject.class, "addCustomObject", "Property=" + property.getPropertyName());
                    logger.warn(JsonObject.class, "addCustomObject", "PropertyType not handled : " + property.getClass().getCanonicalName());
                }
            } catch (NullPointerException e) {
                logger.error(JsonObject.class, "addCustomObject", "Property=" + property.getPropertyName());
                throw e;
            }
        }
    }

    private static void addGroup(ICNLogger logger, JSONObject jsonObj, Group group, String orderingValue) {
        jsonObj.put("id", group.get_Id());
        jsonObj.put("name", group.get_Name());
        jsonObj.put("shortName", group.get_ShortName());
        jsonObj.put("displayName", group.get_DisplayName());
        jsonObj.put("distinguishedName", group.get_DistinguishedName());

        if (!group.get_Users().isEmpty()) {
            JSONArray users = new JSONArray();
            for (Iterator<User> userIterator = group.get_Users().iterator(); userIterator.hasNext(); ) {
                JSONObject userJSON = new JSONObject();
                addUser(userJSON, userIterator.next());
                users.add(userJSON);

            }

            if (!Strings.isNullOrEmpty(orderingValue)) {
                users = sort(logger, users, orderingValue);
            }

            logger.info(JsonObject.class, "addGroup", "Ordered List by :" + orderingValue);

            for (Object object : users) {
                JSONObject user = (JSONObject) object;
                logger.info(JsonObject.class, "addGroup", user.get(orderingValue).toString());
            }

            jsonObj.put("users", users);
        }
    }

    private static void addUser(JSONObject jsonObj, User user) {
        jsonObj.put("id", user.get_Id());
        jsonObj.put("name", user.get_Name());
        jsonObj.put("shortName", user.get_ShortName());
        jsonObj.put("displayName", user.get_DisplayName());
        jsonObj.put("distinguishedName", user.get_DistinguishedName());
        jsonObj.put("emailAddress", user.get_Email());
    }

    private static JSONArray sort(ICNLogger logger, JSONArray jsonArray, final String orderingValue) {
        logger.info(JsonObject.class, "sort", "Ordering Value = " + orderingValue);

        Comparator<JSONObject> stringAlphabeticalComparator = new Comparator<JSONObject>() {
            public int compare(JSONObject object1, JSONObject object2) {
                String value1 = "";
                if (object1.get(orderingValue) != null) {
                    value1 = (String) object1.get(orderingValue);
                }
                String value2 = "";
                if (object2.get(orderingValue) != null) {
                    value2 = (String) object2.get(orderingValue);
                }


                return ComparisonChain.start().
                        compare(value1, value2, String.CASE_INSENSITIVE_ORDER).
                        result();
            }
        };

        List<JSONObject> unsorted = Lists.newArrayList();
        for (Iterator<JSONObject> jsonObjectIterator = jsonArray.iterator(); jsonObjectIterator.hasNext(); ) {
            JSONObject jsonObject = jsonObjectIterator.next();
            unsorted.add(jsonObject);
        }

        Collections.sort(unsorted, stringAlphabeticalComparator);

        logger.info(JsonObject.class, "sort", "Ordered List by :" + orderingValue);

        JSONArray sortedArray = new JSONArray();
        for (JSONObject jsonObject : unsorted) {
            sortedArray.add(jsonObject);
        }

        return sortedArray;
    }

    protected String getSymbolicName() {
        return (String) getJsonObject().get("name");
    }

    public JSONObject getJson() {
        return getJsonObject();
    }
}
