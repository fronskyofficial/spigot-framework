package org.example.logic.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.example.Main;
import org.example.logic.interfaces.IFile;
import org.example.logic.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class YmlFile implements IFile {

    private final Main mainClass;
    private final String fileName;
    private FileConfiguration configuration = null;
    private File file = null;

    public YmlFile(String fileName, Main mainClass) {
        this.fileName = fileName + ".yml";
        this.mainClass = mainClass;

        saveDefaultConfig();
    }

    @Override
    public void reloadFile() {
        if (file == null) {
            file = new File(mainClass.getDataFolder(), fileName);
        }

        configuration = YamlConfiguration.loadConfiguration(file);
        InputStream stream = mainClass.getResource(fileName);
        if (stream != null) {
            InputStreamReader streamReader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(streamReader);
            configuration.setDefaults(yamlConfiguration);
        }
    }

    @Override
    public FileConfiguration getFile() {
        if (configuration == null) {
            reloadFile();
        }

        return configuration;
    }

    @Override
    public void saveFile() {
        if (configuration == null || file == null) {
            return;
        }

        try {
            getFile().save(file);
        } catch (IOException e) {
            Logger.logError(e.getMessage());
        }
    }

    /**
     * Saves the default configuration file if it does not exist.
     * If the file is null, it creates a new file with the given file name in the data folder of the main class.
     */
    private void saveDefaultConfig() {
        if (file == null) {
            file = new File(mainClass.getDataFolder(), fileName);
        }

        if (!file.exists()) {
            mainClass.saveResource(fileName, false);
        }
    }
}
