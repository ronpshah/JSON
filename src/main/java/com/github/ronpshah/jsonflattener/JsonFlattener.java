package com.github.ronpshah.jsonflattener;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by shah_ on 10/18/2017.
 */
public class JsonFlattener {

    /**
     * Convert JSONObject to Map<QualifiedName, Value> with QualifiedName key as String type & Value also String type.
     * QualifiedName - Consist '.' operator to represent sub-level of JSON & '_' followed with number (0, 1, 2..) to represent arrays.
     *                Root element starts with 'JSON' as prefix followed with '.' operator.
     * @param  jsonInput - JSONObject to flatten
     * @return Map<QualifiedNameStringtype, ValueStringType>
     */

    public static Map<String, String> flattenJSONtoMap(JSONObject jsonInput){

        Map<String, String> jsonMap = new HashMap<String, String>();
        StringBuffer sQualifiedName = new StringBuffer("JSON");
        flattenJSONHelper(jsonInput, jsonMap, sQualifiedName);

        return jsonMap;
    }

    /**
     * Helper function to used by flattenJSONtoMap() method
     * @param jsonInput - JSONObject to flatten
     * @param jsonMap - Map to store <QualifiedName, Value>
     * @param sQualifiedName - Prefix with 'JSON'
     */
    private static void flattenJSONHelper(JSONObject jsonInput, Map<String,String> jsonMap, StringBuffer sQualifiedName){
        if (jsonInput == null){
            return;
        }

        for(Iterator iterator = jsonInput.keySet().iterator(); iterator.hasNext();) {
            String sKey = (String) iterator.next();
            Object oTmp = jsonInput.get(sKey);

            if (oTmp != null){
                if (oTmp instanceof JSONObject){
                    sQualifiedName.append("."+sKey);
                    flattenJSONHelper((JSONObject)oTmp, jsonMap, sQualifiedName);
                    sQualifiedName.replace(sQualifiedName.length()-sKey.length()-1, sQualifiedName.length(), "");
                }else if (oTmp instanceof JSONArray){
                    JSONArray jsonArray = (JSONArray) oTmp;
                    Iterator  jsonArrayIterator = jsonArray.iterator();
                    int i = 0;
                    while (jsonArrayIterator.hasNext()){
                        String sKeyWithIndex = sKey + "[" + i + "]";
                        sQualifiedName.append("."+sKeyWithIndex);
                        Object oTmp2 = jsonArrayIterator.next();
                        if (oTmp2 instanceof JSONObject){
                            flattenJSONHelper((JSONObject) oTmp2, jsonMap, sQualifiedName);
                        }else {
                            jsonMap.put(sQualifiedName.toString(), oTmp2.toString());
                        }
                        sQualifiedName.replace(sQualifiedName.length()-sKeyWithIndex.length()-1, sQualifiedName.length(), "");
                        i++;
                    }
                }else {
                    sQualifiedName.append("."+sKey);
                    jsonMap.put(sQualifiedName.toString(), oTmp.toString());
                    sQualifiedName.replace(sQualifiedName.length()-sKey.length()-1, sQualifiedName.length(), "");
                }
            }

        }


    }


}
