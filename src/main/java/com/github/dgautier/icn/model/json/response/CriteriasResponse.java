package com.github.dgautier.icn.model.json.response;

import com.github.dgautier.icn.ICNLogger;
import com.github.dgautier.icn.model.json.Criteria;
import com.google.common.collect.Maps;
import com.ibm.ecm.json.JSONResponse;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by DGA on 21/01/2015.
 */
public class CriteriasResponse extends AbstractResponse {


    private final Map<String, Criteria> criterias = Maps.newHashMap();


    public CriteriasResponse(ICNLogger logger, JSONResponse jsonResponse) {
        super(logger,jsonResponse);


        JSONArray jsonCriterias = (JSONArray) jsonResponse.get("criterias");
        Iterator<JSONObject> jsonCriteriasIterator = jsonCriterias.iterator();
        while (jsonCriteriasIterator.hasNext()) {
            JSONObject criteria = jsonCriteriasIterator.next();
            String symbolicName = (String) criteria.get("name");
            this.criterias.put(symbolicName, new Criteria(logger, criteria));
            logger.debug(CriteriasResponse.class, "constructor", "symbolicName=" + symbolicName);
        }

    }

    public Criteria getCriteria(String symbolicName) {
        return this.criterias.get(symbolicName);
    }

    public boolean hasCriteria(String symbolicName) {
        return this.criterias.containsKey(symbolicName);
    }

    public void order(List<String> order) {


        List<JSONObject> orderedItems = new ArrayList<JSONObject>();
        JSONArray jsonCriterias = (JSONArray) getJsonResponse().get("criterias");

        // Add ordered Items firsts
        for (String symbolicName : order) {
            Criteria criteria = response.getCriteria(symbolicName);
            if (criteria != null) {
                orderedItems.add(criteria.getJson());
            }
        }

        // then add other items
        for (Object item : jsonCriterias) {
            JSONObject jsonObject = (JSONObject) item;
            if (!order.contains(jsonObject.get("name"))) {
                orderedItems.add(jsonObject);
            }
        }

        // Convert List to Array
        JSONArray orderedCriterias = new JSONArray();
        for (JSONObject jsonObject : orderedItems) {
            orderedCriterias.add(jsonObject);
        }

        getJsonResponse().put("criterias", orderedCriterias);



    }

}
