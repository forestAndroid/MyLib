package com.cmhi.log.helper.encryptUtil;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    public JsonUtils() {
    }

    public static String JSONTokener(String in) {
        if (in != null && in.startsWith("\ufeff")) {
            in = in.substring(1);
        }

        return in;
    }

    public static Map<String, Object> getJosn(String jsonStr) throws Exception {
        Map<String, Object> map = new HashMap();
        if (!TextUtils.isEmpty(jsonStr)) {
            JSONObject json = new JSONObject(jsonStr);
            Iterator i = json.keys();

            while(true) {
                while(i.hasNext()) {
                    String key = (String)i.next();
                    Object value = json.get(key);
                    if (value != null && value.toString().indexOf("{") == 0) {
                        map.put(key.trim(), getJosn(value.toString()));
                    } else if (value != null && value.toString().indexOf("[") == 0) {
                        map.put(key.trim(), getList(value.toString()));
                    } else {
                        map.put(key.trim(), String.valueOf(value).trim());
                    }
                }

                return map;
            }
        } else {
            return map;
        }
    }

    public static List<Map<String, Object>> getList(String jsonStr) throws Exception {
        List<Map<String, Object>> list = new ArrayList();
        JSONArray ja = new JSONArray(jsonStr);

        for(int j = 0; j < ja.length(); ++j) {
            String jm = ja.get(j) + "";
            if (jm.indexOf("{") == 0) {
                Map<String, Object> m = getJosn(jm);
                list.add(m);
            }
        }

        return list;
    }
}
