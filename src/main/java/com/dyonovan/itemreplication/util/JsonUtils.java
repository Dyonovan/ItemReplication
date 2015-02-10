package com.dyonovan.itemreplication.util;

import com.dyonovan.itemreplication.ItemReplication;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class JsonUtils {

    public static boolean writeJson(HashMap<String, Integer> values, String modID) {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(values);

        try {
            FileWriter fw = new FileWriter(ReplicatorUtils.fileDirectory + modID + ".json");
            fw.write(json);
            fw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static HashMap<String, Integer> readJson(String modID) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(ReplicatorUtils.fileDirectory + modID + ".json"));
            Gson gson = new Gson();
            Map<String, Integer> map = gson.fromJson(br, new TypeToken<Map<String, Integer>>(){}.getType());
            if (map != null) {
                HashMap<String, Integer> hashMap = (map instanceof HashMap) ? (HashMap) map : new HashMap<String, Integer>(map);
                return hashMap.size() == 0 ? null : hashMap;
            } else {
                return null;
            }
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
