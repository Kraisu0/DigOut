package com.kraisu.digout;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonLoader {

    public static void loadAndDisplayJsonData(String filePath) {
        List<String> names = new ArrayList<>();
        Map<String, List<String>> descriptions = new HashMap<>();

        try {
            JsonReader jsonReader = new JsonReader();
            JsonValue jsonData = jsonReader.parse(Gdx.files.internal(filePath));

            // Wczytaj listę imion
            JsonValue namesArray = jsonData.get("names");
            for (JsonValue name : namesArray) {
                names.add(name.asString());
            }

            // Wczytaj opisy
            JsonValue descriptionsObject = jsonData.get("descriptions");
            for (JsonValue descriptionType : descriptionsObject) {
                List<String> descriptionList = new ArrayList<>();
                for (JsonValue description : descriptionType) {
                    descriptionList.add(description.asString());
                }
                descriptions.put(descriptionType.name, descriptionList);
            }

            // Wyświetlanie wyników w konsoli
            System.out.println("Names:");
            for (String name : names) {
                System.out.println(name);
            }

            System.out.println("\nDescriptions:");
            for (Map.Entry<String, List<String>> entry : descriptions.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

