package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static List<String> getStringList(JSONArray jsonArray) throws JSONException {
        int length = jsonArray.length();
        List<String> result = new ArrayList<>(length);
        for (int index = 0; index < length; ++index) {
            result.add(jsonArray.getString(index));
        }
        return result;
    }

    public static Sandwich parseSandwichJson(String json) {
        try {
            JSONObject sandwichJson = new JSONObject(json);
            JSONObject nameJson = sandwichJson.getJSONObject("name");

            return new Sandwich(
                    nameJson.getString("mainName"),
                    getStringList(nameJson.getJSONArray("alsoKnownAs")),
                    sandwichJson.getString("placeOfOrigin"),
                    sandwichJson.getString("description"),
                    sandwichJson.getString("image"),
                    getStringList(sandwichJson.getJSONArray("ingredients"))
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
