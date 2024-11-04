package com.kraisu.digout.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.kraisu.digout.logs.DateLogs;

import java.util.ArrayList;
import java.util.List;

import static com.kraisu.digout.help.ConstantsFileDirectory.filenames.NAME_DESCRIPTION_JSON;
import static com.kraisu.digout.help.ConstantsGenerator.JsonData.descriptions;
import static com.kraisu.digout.help.ConstantsGenerator.JsonData.names;
import static com.kraisu.digout.logs.DateLogs.logs;

public class JsonLoader {

    private static void loadAndDisplayJsonData(String filePath) {

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


        } catch (Exception e) {
            logs(DateLogs.LogType.ERROR, null, "Error while loading json data from file " + filePath, e);
            System.out.println("Error while loading json data from file " + filePath);
            System.out.println("Names : " + names);
            System.out.println("Descriptions : " + descriptions);
            e.printStackTrace();
        }
    }

    public static void mainLoader(){
        loadAndDisplayJsonData(NAME_DESCRIPTION_JSON);
    }
}
