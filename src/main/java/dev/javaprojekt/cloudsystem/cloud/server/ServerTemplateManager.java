package dev.javaprojekt.cloudsystem.cloud.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.javaprojekt.cloudsystem.file.FileManager;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ServerTemplateManager {

    private static ServerTemplateManager instance;

    private HashMap<String, ServerTemplate> templates = new HashMap<>();

    public ServerTemplateManager() {
        instance = this;
    }

    public static ServerTemplateManager getInstance() {
        return instance;
    }

    public HashMap<String, ServerTemplate> getTemplates() {
        return templates;
    }

    public void importTemplates() throws IOException {
        getTemplates().clear();
        File file = new File(FileManager.getConfigDirectory().toString() + "/templates/");
        if (!file.exists()) {
            System.out.println("File " + file.toString() + " does not exist.");
        }
        for (File listFile : file.listFiles()) {
            ServerTemplate template = new Gson().fromJson(new FileReader(listFile), ServerTemplate.class);
            if (template != null)
                getTemplates().put(template.getName(), template);
        }
    }

    public void createTemplate(ServerTemplate template) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(template);
        FileManager.writeToFile(new File(FileManager.getConfigDirectory().toString() + "/templates/" + template.getName() + ".json"), jsonString);
        if (!FileManager.getTemplatesDirectory(template.getName()).exists()) {
            FileManager.getTemplatesDirectory(template.getName()).mkdirs();
        }
    }

}
