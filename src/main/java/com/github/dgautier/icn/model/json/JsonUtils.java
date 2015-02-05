package com.github.dgautier.icn.model.json;

import com.filenet.api.admin.LocalizedString;
import com.filenet.api.collection.ChoiceList;
import com.filenet.api.collection.LocalizedStringList;
import com.filenet.api.constants.TypeID;
import com.github.dgautier.icn.ICNLogger;
import com.google.common.base.Function;
import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.Locale;
import java.util.zip.GZIPOutputStream;

/**
 * Created by DGA on 22/01/2015.
 */
public class JsonUtils {

    public static String getConfiguration(String configuration, String key) throws Exception {
        JSONObject configurationJSON = JSONObject.parse(configuration);
        JSONArray array = (JSONArray) configurationJSON.get("configuration");
        for (Object entry : array) {
            JSONObject jsonObject = (JSONObject) entry;
            String name = (String) jsonObject.get("name");
            if (name.equalsIgnoreCase(key)) {
                return (String) jsonObject.get("value");
            }
        }

        throw new IllegalArgumentException("No configuration found for key=" + key);
    }

    public static JSONObject toJSONObject(ICNLogger logger, Locale locale, com.filenet.api.admin.ChoiceList choiceList, Function<JSONObject, JSONObject> optionalProperty) {
        logger.debug(Criteria.class, "toJSONObject", "toJSONObject");
        JSONObject jsonChoiceList = new JSONObject();
        jsonChoiceList.put("displayName", choiceList.get_DisplayName());

        JSONArray jsonChoices = new JSONArray();
        JSONArray choiceListValues = getChoiceValuesAsLeafs(jsonChoices, locale, choiceList.get_ChoiceValues(), choiceList.get_DataType(), optionalProperty);
        jsonChoiceList.put("values", choiceListValues);
        logger.debug(Criteria.class, "toJSONObject", "values size =" + choiceListValues.size());
        return jsonChoiceList;

    }

    public static JSONArray getChoiceValues(Locale locale, com.filenet.api.admin.ChoiceList choiceList, Function<JSONObject, JSONObject> optionalProperty) {
        JSONArray choiceListValues = getChoiceValues(locale, choiceList.get_ChoiceValues(), choiceList.get_DataType(),optionalProperty);
        return choiceListValues;
    }

    public static JSONObject getChoiceList(com.filenet.api.admin.ChoiceList choiceList,JSONArray choiceListValues) {

        JSONObject jsonChoiceList = new JSONObject();
        jsonChoiceList.put("displayName", choiceList.get_DisplayName());
        jsonChoiceList.put("choices",choiceListValues );

        return jsonChoiceList;
    }

    private static JSONArray getChoiceValues(Locale locale, ChoiceList cl, TypeID typeID, Function<JSONObject, JSONObject> optionalProperty) {
        JSONArray jsonChoices = new JSONArray();
        Iterator itr = cl.iterator();
        while (itr.hasNext()) {
            com.filenet.api.admin.Choice c = (com.filenet.api.admin.Choice) itr.next();
            String cValue = null;
            if (typeID.equals(TypeID.LONG)) {
                if (c.get_ChoiceIntegerValue() != null) {
                    cValue = c.get_ChoiceIntegerValue() + "";
                }
            } else if (typeID.equals(TypeID.STRING)) {
                cValue = c.get_ChoiceStringValue();
            }
            JSONObject jsonChoice = new JSONObject();
            String displayName = JsonUtils.getLocalizedText(c, locale);
            jsonChoice.put("displayName", displayName);
            if (cValue != null) {
                jsonChoice.put("value", cValue);
                if (optionalProperty != null){
                    jsonChoice = optionalProperty.apply(jsonChoice);
                }
            } else {
                jsonChoice.put("choices", getChoiceValues(locale, c.get_ChoiceValues(), typeID, optionalProperty));
            }



            jsonChoices.add(jsonChoice);
        }
        return jsonChoices;
    }

    private static JSONArray getChoiceValuesAsLeafs(JSONArray jsonChoices, Locale locale, ChoiceList cl, TypeID typeID, Function<JSONObject, JSONObject> optionalProperty) {

        Iterator itr = cl.iterator();
        while (itr.hasNext()) {
            com.filenet.api.admin.Choice c = (com.filenet.api.admin.Choice) itr.next();
            String cValue = null;
            if (typeID.equals(TypeID.LONG)) {
                if (c.get_ChoiceIntegerValue() != null) {
                    cValue = c.get_ChoiceIntegerValue() + "";
                }
            } else if (typeID.equals(TypeID.STRING)) {
                cValue = c.get_ChoiceStringValue();
            }
            JSONObject jsonChoice = new JSONObject();
            String displayName = getLocalizedText(c, locale);
            jsonChoice.put("displayName", displayName);
            if (cValue != null) {
                jsonChoice.put("value", cValue);
                if (optionalProperty != null) {
                    jsonChoice = optionalProperty.apply(jsonChoice);
                }
            } else {
                getChoiceValuesAsLeafs(jsonChoices, locale, c.get_ChoiceValues(), typeID, optionalProperty);
            }
            jsonChoices.add(jsonChoice);
        }
        return jsonChoices;
    }

    public static String getLocalizedText(com.filenet.api.admin.Choice choice, Locale locale) {
        String displayName = choice.get_DisplayName();
        if (choice.getProperties().isPropertyPresent("DisplayNames")) {
            String language = locale.getLanguage();
            String localeName = locale.getLanguage() + "-" + locale.getCountry();
            LocalizedStringList names = choice.get_DisplayNames();
            Iterator i = names.iterator();
            boolean found = false;
            while (i.hasNext()) {
                LocalizedString name = (LocalizedString) i.next();
                if (name.get_LocaleName().equalsIgnoreCase(localeName)) {
                    displayName = name.get_LocalizedText();
                    found = true;
                    break;
                }
            }
            if (!found) {
                i = names.iterator();
                while (i.hasNext()) {
                    LocalizedString name = (LocalizedString) i.next();
                    if (name.get_LocaleName().equalsIgnoreCase(language)) {
                        displayName = name.get_LocalizedText();
                        break;
                    }
                }
            }
        }
        return displayName;
    }
}
